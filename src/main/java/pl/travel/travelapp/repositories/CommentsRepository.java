package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.Comments;
@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {
}
