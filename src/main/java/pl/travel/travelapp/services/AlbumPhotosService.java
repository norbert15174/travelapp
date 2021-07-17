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
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
public class AlbumPhotosService implements PhotoInterface {
    private AlbumPhotosRepository albumPhotosRepository;
    private IndividualAlbumInterface individualAlbumService;

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    @Autowired
    public AlbumPhotosService(AlbumPhotosRepository albumPhotosRepository , IndividualAlbumInterface individualAlbumService) {
        this.albumPhotosRepository = albumPhotosRepository;
        this.individualAlbumService = individualAlbumService;
    }




    @Transactional
    public ResponseEntity <AlbumDTO> addNewPhotoToAlbum(Principal principal, MultipartFile file, long id, String description){
        IndividualAlbum individualAlbum = individualAlbumService.findUserAlbum(principal, id);
        if(individualAlbum != null){
            try {
                String path = "user/" + principal.getName() + "/album/" + id + "/picture/" + file.getOriginalFilename();
                BlobId blobId = BlobId.of(bucket , path);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                storage.create(blobInfo , file.getBytes());
                AlbumPhotos albumPhotos = new AlbumPhotos();
                albumPhotos.setPhotoUrl(url + path);
                albumPhotos.setDescription(description);
                albumPhotos.setIndividualAlbum(individualAlbum);
                individualAlbum.addNewPhoto(albumPhotos);
                individualAlbumService.saveAlbum(individualAlbum);
                AlbumDTO albumDTO = new AlbumDTO().build(individualAlbum);
                return new ResponseEntity (albumDTO, HttpStatus.CREATED);
            } catch ( IOException e ) {
                e.printStackTrace();
                return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity <>(HttpStatus.FORBIDDEN);
    }

    @Override
    public List <AlbumPhotos> findUserPhotos(long albumId , long page) {
        return null;
    }

    @Override
    public ResponseEntity <List <AlbumPhotos>> findUserPhotos(Principal principal , long albumId , long page) {
        return null;
    }
}
