package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.groups.GroupCommentsDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoDTO;
import pl.travel.travelapp.entites.GroupPhoto;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupPhotoRepository extends JpaRepository <GroupPhoto, Long> {
    @Query("select new pl.travel.travelapp.DTO.groups.GroupPhotoDTO(gp) from GroupPhoto gp " +
            "left join gp.comments " +
            "left join gp.tagged " +
            "inner join gp.owner where gp.album.id = :groupAlbumId order by gp.dateTime desc")
    List <GroupPhotoDTO> getGroupPhotoDTOByGroupAlbumId(@Param("groupAlbumId") Long groupAlbumId , Pageable page);

    @Query("select gp from GroupPhoto gp " +
            "left join fetch gp.comments " +
            "left join fetch gp.tagged " +
            "inner join fetch gp.owner where gp.id = :photoId")
    Optional <GroupPhoto> getPhotoById(@Param("photoId") Long photoId);

}
