package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.GroupPhoto;

@Repository
public interface GroupPhotoRepository extends JpaRepository <GroupPhoto, Long> {
}
