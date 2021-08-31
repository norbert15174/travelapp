package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.Comments;

import java.util.Set;

@Repository
public interface CommentsRepository extends JpaRepository <Comments, Long> {

    @Query("SELECT c FROM Comments c WHERE c.userId = :id")
    Set <Comments> findCommentsByUserId(Long id);

}
