package pl.travel.travelapp.services.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.photos.PhotoDTO;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.Comments;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.TaggedUser;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;
import pl.travel.travelapp.services.query.interfaces.IPhotoQueryService;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class PhotoQueryService implements IPhotoQueryService {

    private final AlbumPhotosRepository albumPhotosRepository;

    public PhotoQueryService(AlbumPhotosRepository albumPhotosRepository) {
        this.albumPhotosRepository = albumPhotosRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional <AlbumPhotos> findById(Long id) {
        return albumPhotosRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public AlbumPhotos findByIdAndPhotoOwner(Long id , PersonalData user) {
        return albumPhotosRepository.findPhotoByOwnerAndPhotoId(user.getId() , id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public List <Comments> findCommentsByPhotoId(Long id , Long userId) {
        Optional <AlbumPhotos> photo = albumPhotosRepository.findCommentsByPhotoId(id , userId);
        if ( photo.isPresent() ) {
            return photo.get().getComments();
        } else {
            return new ArrayList <>();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Set <TaggedUser> findTaggedUsersByPhotoId(Long id , Long userId) {
        Optional <AlbumPhotos> photo = albumPhotosRepository.findCommentsByPhotoId(id , userId);
        if ( photo.isPresent() ) {
            return photo.get().getTaggedList();
        } else {
            return new HashSet <>();
        }
    }

    @Override
    public Optional <PhotoDTO> getPhoto(Long userId , Long photoId) {
        return albumPhotosRepository.getPhotoById(userId,photoId);
    }
}
