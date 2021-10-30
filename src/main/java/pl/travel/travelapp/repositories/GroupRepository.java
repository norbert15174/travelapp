package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.entites.UsersGroup;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository <UsersGroup, Long> {
    @Query("SELECT g FROM UsersGroup g inner join fetch g.owner inner join fetch g.members where g.id = :groupId")
    Optional <UsersGroup> findGroupById(Long groupId);

    @Query("SELECT distinct new pl.travel.travelapp.DTO.groups.GroupGetDTO(g, true) FROM UsersGroup g inner join g.members m where m.id = :userId")
    List <GroupGetDTO> findUserGroups(@Param("userId") Long userId);
}
