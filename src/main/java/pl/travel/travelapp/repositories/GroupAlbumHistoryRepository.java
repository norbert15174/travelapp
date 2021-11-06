package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;
import pl.travel.travelapp.entites.GroupAlbumHistory;

import java.util.List;

@Repository
public interface GroupAlbumHistoryRepository extends JpaRepository <GroupAlbumHistory, Long> {
    @Query("select new pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO(h) from GroupAlbumHistory h inner join h.user where h.album.id = :groupAlbumId")
    List <GroupAlbumHistoryDTO> getAlbumHistoryByGroupAlbumId(@Param("groupAlbumId") Long groupAlbumId , Pageable page);
}
