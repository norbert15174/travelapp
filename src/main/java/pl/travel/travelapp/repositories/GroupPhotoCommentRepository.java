package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.groups.GroupCommentsDTO;
import pl.travel.travelapp.entites.GroupPhotoComments;

import java.util.List;

@Repository
public interface GroupPhotoCommentRepository extends JpaRepository <GroupPhotoComments, Long> {
    @Query("select new pl.travel.travelapp.DTO.groups.GroupCommentsDTO(gc) from GroupPhotoComments gc " +
            "inner join gc.photo p inner join gc.commentedBy where p.id = :photoId order by gc.dateTime desc")
    List <GroupCommentsDTO> findPhotoCommentsByPhotoId(Long photoId);
}
