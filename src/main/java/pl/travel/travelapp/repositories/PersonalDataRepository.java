package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.PersonalData;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality where p.id = :idUser")
    PersonalData findPersonalDataByUserId(long idUser);
}
