package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.AlbumPhotos;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumPhotosRepository extends JpaRepository <AlbumPhotos, Long> {

    @Query("select p from AlbumPhotos p left join p.individualAlbum a where a.id=:id")
    List <AlbumPhotos> findAlbumsPhotosByAlbumId(long id);

    @Query("select p from AlbumPhotos p" +
            " left join p.individualAlbum a" +
            " left join a.owner o " +
            "where p.photoId=:photoId and o.id=:ownerId")
    Optional <AlbumPhotos> findPhotoByOwnerAndPhotoId(Long ownerId , Long photoId);

    @Query("select p from AlbumPhotos p " +
            "left join p.individualAlbum a " +
            "left join a.owner o " +
            "left join a.sharedAlbum s " +
            "where p.photoId=:id and (o.id=:userId or a.isPublic=true or s.userId=:userId)")
    Optional <AlbumPhotos> findCommentsByPhotoId(@Param("id") Long id , @Param("userId") Long userId);
}
