package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.PersonalDescription;

@Repository
public interface PersonalDescriptionRepository extends JpaRepository<PersonalDescription,Long> {
}
