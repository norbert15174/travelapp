package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO;
import pl.travel.travelapp.entites.GroupAlbum;

@Repository
public interface GroupAlbumRepository extends JpaRepository <GroupAlbum, Long> {

    @Query("select new pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO(ga) from GroupAlbum ga " +
            "inner join ga.photos p inner join p.tagged inner join p.comments inner join p.owner where ga.id = :groupAlbumId")
    GroupAlbumFullDTO findGroupAlbumByIdWithPhotos(@Param("groupAlbumId") Long groupAlbumId);
}
