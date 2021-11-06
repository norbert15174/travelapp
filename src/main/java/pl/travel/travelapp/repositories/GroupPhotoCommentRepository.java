package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.GroupPhotoComments;

@Repository
public interface GroupPhotoCommentRepository extends JpaRepository<GroupPhotoComments, Long> {
}
