package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.PersonalData;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
}
