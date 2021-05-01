package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.IndividualAlbum;

@Repository
public interface IndividualAlbumRepository extends JpaRepository<IndividualAlbum,Long> {
}
