package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.AlbumPhotos;

@Repository
public interface AlbumPhotosRepository extends JpaRepository<AlbumPhotos,Long> {

}
