package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO;
import pl.travel.travelapp.entites.GroupAlbum;

@Repository
public interface GroupAlbumRepository extends JpaRepository <GroupAlbum, Long> {

    @Query("select distinct new pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO(ga) from GroupAlbum ga " +
            "left join ga.photos p left join p.tagged left join p.comments left join p.owner where ga.id = :groupAlbumId")
    GroupAlbumFullDTO findGroupAlbumByIdWithPhotos(@Param("groupAlbumId") Long groupAlbumId);
}
