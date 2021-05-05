package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.Comments;
@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {
}
