package pl.travel.travelapp.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.Comments;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentsRepository extends JpaRepository <Comments, Long> {

    @Query("SELECT c FROM Comments c WHERE c.userId = :id")
    Set <Comments> findCommentsByUserId(Long id);

    @Query("SELECT distinct ap FROM AlbumPhotos ap inner join ap.comments c left join fetch ap.individualAlbum ia left join fetch ia.owner o where o.id = :userId order by c.time desc")
    List<AlbumPhotos> findPhotoUserComment(Long userId , Pageable page);
}
