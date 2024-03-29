package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.PersonalData;

import java.util.Optional;
import java.util.Set;


@Repository
public interface PersonalDataRepository extends JpaRepository <PersonalData, Long>, JpaSpecificationExecutor <PersonalData> {
    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality where p.id = :idUser")
    PersonalData findPersonalDataByUserId(long idUser);

    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality left join fetch p.albums where p.id = :idUser")
    PersonalData findPersonalDataWithAlbumsByUserId(long idUser);

    @Query("SELECT distinct p from PersonalData p " +
            "left join fetch p.personalDescription " +
            "left join fetch p.Nationality " +
            "left join fetch p.albums al " +
            "left join fetch al.owner o " +
            "where p.id = :idUser")
    Optional <PersonalData> findPersonalDataByUserIdWithSharedAndOwnedAlbums(@Param("idUser") long idUser , @Param("id") long id);

    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality where p.id = :idUser")
    Optional <PersonalData> findPersonalDataOptionalById(long idUser);

    @Query("SELECT p from PersonalData p left join fetch p.personalDescription left join fetch p.Nationality where p.id in :idUsers")
    Set <PersonalData> getUsersByIds(Set <Long> idUsers);
}
