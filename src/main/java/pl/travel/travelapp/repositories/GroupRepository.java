package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.UsersGroup;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository <UsersGroup, Long> {
    @Query("SELECT g FROM UsersGroup g inner join fetch g.owner inner join fetch g.members where g.id = :groupId")
    Optional <UsersGroup> findGroupById(Long groupId);
}
