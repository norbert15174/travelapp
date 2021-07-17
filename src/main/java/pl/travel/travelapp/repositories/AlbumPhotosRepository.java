package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.AlbumPhotos;


import java.util.List;

@Repository
public interface AlbumPhotosRepository extends JpaRepository<AlbumPhotos,Long> {

    @Query("select p from AlbumPhotos p left join p.individualAlbum a where a.id=:id")
    List <AlbumPhotos> findAlbumsPhotosByAlbumId(long id);

}
