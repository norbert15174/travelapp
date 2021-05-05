package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.SharedAlbum;
@Repository
public interface SharedAlbumRepository extends JpaRepository<SharedAlbum,Long> {
}
