package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.GroupAlbum;

@Repository
public interface GroupAlbumRepository extends JpaRepository <GroupAlbum, Long> {
}
