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
import pl.travel.travelapp.models.*;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;
import pl.travel.travelapp.repositories.CommentsRepository;
import pl.travel.travelapp.services.delete.interfaces.IPhotoDeleteService;
import pl.travel.travelapp.services.query.FriendsQueryService;
import pl.travel.travelapp.services.query.interfaces.IPhotoQueryService;
import pl.travel.travelapp.services.save.interfaces.IPhotoSaveService;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumPhotosService implements PhotoInterface {
    private IndividualAlbumInterface individualAlbumService;
    private PersonalService personalService;
    private FriendsQueryService friendsQueryService;
    private final IPhotoQueryService photoQueryService;
    private final IPhotoSaveService photoSaveService;
    private final IPhotoDeleteService photoDeleteService;


    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    public AlbumPhotosService(IndividualAlbumInterface individualAlbumService , PersonalService personalService , FriendsQueryService friendsQueryService , IPhotoQueryService photoQueryService , IPhotoSaveService photoSaveService , IPhotoDeleteService photoDeleteService) {
        this.individualAlbumService = individualAlbumService;
        this.personalService = personalService;
        this.friendsQueryService = friendsQueryService;
        this.photoQueryService = photoQueryService;
        this.photoSaveService = photoSaveService;
        this.photoDeleteService = photoDeleteService;
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
            Optional <AlbumPhotos> albumPhoto = photoQueryService.findById(id);
            if ( albumPhoto.isPresent() ) {
                PersonalData userData = albumPhoto.get().getIndividualAlbum().getOwner();
                if ( userData.getId() != user.getId() ) return new ResponseEntity(HttpStatus.FORBIDDEN);
                albumPhoto.get().deletePhotoFromAlbum(albumPhoto.get());
                photoDeleteService.delete(albumPhoto.get());
                BlobId blobId = BlobId.of(bucket , albumPhoto.get().getPhotoName());
                storage.delete(blobId);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity addCommentToPhoto(Principal principal , long photoId , Comments comment) {
        Optional <AlbumPhotos> photo = photoQueryService.findById(photoId);
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        if ( photo.isPresent() ) {
            AlbumPhotos photoToSave = photo.get();
            if ( checkIfUserCanAddComment(photoToSave , user) ) {
                Comments photoComment = new Comments(comment.getText() , user);
                photoToSave.addComment(photoComment);
                photoSaveService.save(photoToSave);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @Transactional
    public ResponseEntity addTaggedUsersToPhoto(Set <Long> ids , Long photoId , Principal principal) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional <AlbumPhotos> albumPhotoOpt = photoQueryService.findById(photoId);
        if ( !albumPhotoOpt.isPresent() ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        AlbumPhotos albumPhoto = albumPhotoOpt.get();
        if ( albumPhoto.getIndividualAlbum().getOwner().getId() != user.getId() ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        List <Friends> friends = friendsQueryService.findFriendsByUserId(user.getId());
        Set <PersonalData> userToTag = new HashSet <>();
        for (Friends f : friends) {
            if ( f.getFirstUser().getId() == user.getId() ) {
                userToTag.add(f.getSecondUser());
            } else if ( f.getSecondUser().getId() == user.getId() ) {
                userToTag.add(f.getFirstUser());
            }
        }
        Set <Long> userSharedIds = albumPhoto.getIndividualAlbum().getSharedAlbum().stream().map(SharedAlbum::getUserId).collect(Collectors.toSet());
        Set <PersonalData> userTagged = userToTag.stream().filter(tag -> userSharedIds.contains(tag.getId())).collect(Collectors.toSet());
        albumPhoto.addTaggedUser(new TaggedUser().buildTaggedUsers(userTagged));
        photoSaveService.save(albumPhoto);
        return new ResponseEntity(HttpStatus.OK);
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
