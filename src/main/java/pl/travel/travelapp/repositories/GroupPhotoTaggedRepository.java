package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.GroupPhotoTagged;

@Repository
public interface GroupPhotoTaggedRepository extends JpaRepository <GroupPhotoTagged, Long> {
}
