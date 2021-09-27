package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.photos.PhotoDTO;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.Comments;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.TaggedUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IPhotoQueryService {

    Optional <AlbumPhotos> findById(Long id);

    AlbumPhotos findByIdAndPhotoOwner(Long id , PersonalData user);

    List <Comments> findCommentsByPhotoId(Long id , Long userId);

    Set <TaggedUser> findTaggedUsersByPhotoId(Long id , Long userId);

    Optional <PhotoDTO> getPhoto(Long userId , Long photoId);
}
