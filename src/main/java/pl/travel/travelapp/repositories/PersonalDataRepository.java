package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.travel.travelapp.models.PersonalData;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
}
