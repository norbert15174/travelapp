package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.UsersGroup;

@Repository
public interface GroupRepository extends JpaRepository<UsersGroup, Long> {
}
