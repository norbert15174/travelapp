package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.travel.travelapp.models.TaggedUser;

public interface TaggedPhotoRepository extends JpaRepository<TaggedUser, Long> {
}
