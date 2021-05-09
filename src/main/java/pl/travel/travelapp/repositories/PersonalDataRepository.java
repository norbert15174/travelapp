package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.PersonalData;

import java.util.Optional;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality where p.id = :idUser")
    PersonalData findPersonalDataByUserId(long idUser);
    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality left join fetch p.albums where p.id = :idUser")
    PersonalData findPersonalDataWithAlbumsByUserId(long idUser);
}
