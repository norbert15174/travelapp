package pl.travel.travelapp.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.interfaces.PhotoInterface;
import pl.travel.travelapp.models.AlbumPhotos;
import pl.travel.travelapp.models.Comments;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;
import pl.travel.travelapp.repositories.CommentsRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumPhotosService implements PhotoInterface {
    private AlbumPhotosRepository albumPhotosRepository;
    private IndividualAlbumInterface individualAlbumService;
    private PersonalService personalService;
    private CommentsRepository commentsRepository;


    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    @Autowired
    public AlbumPhotosService(AlbumPhotosRepository albumPhotosRepository , IndividualAlbumInterface individualAlbumService , PersonalService personalService) {
        this.albumPhotosRepository = albumPhotosRepository;
        this.individualAlbumService = individualAlbumService;
        this.personalService = personalService;
    }


    @Transactional
    public ResponseEntity <AlbumDTO> addNewPhotoToAlbum(Principal principal , MultipartFile file , long id , String description) {
        IndividualAlbum individualAlbum = individualAlbumService.findUserAlbum(principal , id);
        if ( individualAlbum != null ) {
            try {
                String path = "user/" + principal.getName() + "/album/" + id + "/picture/" + file.getOriginalFilename();
                BlobId blobId = BlobId.of(bucket , path);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                storage.create(blobInfo , file.getBytes());
                AlbumPhotos albumPhotos = new AlbumPhotos();
                albumPhotos.setPhotoUrl(url + path);
                if ( description != null ) {
                    albumPhotos.setDescription(description);
                }
                albumPhotos.setIndividualAlbum(individualAlbum);
                individualAlbum.addNewPhoto(albumPhotos);
                albumPhotos.setPhotoName(path);
                individualAlbumService.saveAlbum(individualAlbum);
                AlbumDTO albumDTO = new AlbumDTO().build(individualAlbum);
                return new ResponseEntity(albumDTO , HttpStatus.CREATED);
            } catch ( IOException e ) {
                e.printStackTrace();
                return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity <>(HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity <AlbumDTO> addPhotosToAlbum(Principal principal , MultipartFile[] files , long id) {
        for (MultipartFile file : files) {
            if ( addNewPhotoToAlbum(principal , file , id , null).getStatusCode() == HttpStatus.FORBIDDEN )
                return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(new AlbumDTO().build(individualAlbumService.findIndividualAlbumByIdOnlyOwner(principal , id).getBody()) , HttpStatus.OK);
    }

    @Override
    public List <AlbumPhotos> findUserPhotos(long albumId , long page) {
        return null;
    }

    @Override
    public ResponseEntity <List <AlbumPhotos>> findUserPhotos(Principal principal , long albumId , long page) {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity deleteUsersPhotos(List <Long> photoIds , Principal principal) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        if ( user == null ) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        for (Long id : photoIds) {
            Optional <AlbumPhotos> albumPhoto = albumPhotosRepository.findById(id);
            if ( albumPhoto.isPresent() ) {
                PersonalData userData = albumPhoto.get().getIndividualAlbum().getOwner();
                if ( userData.getId() != user.getId() ) return new ResponseEntity(HttpStatus.FORBIDDEN);
                albumPhoto.get().deletePhotoFromAlbum(albumPhoto.get());
                albumPhotosRepository.delete(albumPhoto.get());
                BlobId blobId = BlobId.of(bucket , albumPhoto.get().getPhotoName());
                storage.delete(blobId);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity addCommentToPhoto(Principal principal , long photoId , Comments comment) {
        Optional <AlbumPhotos> photo = albumPhotosRepository.findById(photoId);
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        if ( photo.isPresent() ) {
            AlbumPhotos photoToSave = photo.get();
            if ( checkIfUserCanAddComment(photoToSave , user) ) {
                Comments photoComment = new Comments(comment.getText() , user);
                photoToSave.addComment(photoComment);
                albumPhotosRepository.save(photoToSave);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    private boolean checkIfUserCanAddComment(AlbumPhotos photoToSave , PersonalData user) {
        if ( photoToSave.getTaggedList().stream().anyMatch(tagged -> tagged.getUserId() == user.getId()) ) return true;
        if ( photoToSave.getIndividualAlbum().getOwner().getId() == user.getId() ) return true;
        if ( photoToSave.getIndividualAlbum().isPublic() ) return true;
        if ( photoToSave.getIndividualAlbum().getSharedAlbum().stream().anyMatch(shared -> shared.getUserId() == user.getId()) )
            return true;
        return false;
    }
}
