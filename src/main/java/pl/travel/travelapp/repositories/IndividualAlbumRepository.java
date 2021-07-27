package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.IndividualAlbum;


import java.util.List;
import java.util.Optional;

@Repository
public interface IndividualAlbumRepository extends JpaRepository<IndividualAlbum,Long> {
    @Query("select a from IndividualAlbum a left join fetch a.owner o where o.id =:id and a.isPublic = true")
    List <IndividualAlbum> findUserPublicAlbumsByUserId(long id);
    @Query("select a from IndividualAlbum a left join fetch a.owner o where a.name=:name and a.isPublic = true")
    List <IndividualAlbum> findUserAlbumsByAlbumName(String name, Pageable pageable);
    @Query("select a from IndividualAlbum  a left join fetch a.owner o left join fetch a.sharedAlbum s where a.id =:id and (a.owner.id =:userId or s.userId =:userId or a.isPublic = true)")
    Optional <IndividualAlbum> findIndividualAlbumById(long id, long userId);
    @Query("select a from IndividualAlbum a left join fetch a.owner o where o.id =:id")
    List <IndividualAlbum> findUserAlbumsByUserId(long id);
    //TODO CHECK IF WORK WELL
    @Query("select a from IndividualAlbum a left join fetch a.owner o left join fetch a.sharedAlbum s where a.id =:id and (a.owner.id =:userId or s.userId =:userId or a.isPublic = true)")
    Optional <IndividualAlbum> findIndividualAlbumByIdAndReturnFullInformation(long id, long userId);
    @Query("select a from IndividualAlbum a left join fetch a.owner o left join fetch a.sharedAlbum s where a.id =:id and (o.id =:userId)")
    Optional <IndividualAlbum> findIndividualAlbumByOwner(long id, long userId);

    @Query("select a from IndividualAlbum a" +
            " left join fetch a.owner o" +
            " left join fetch a.sharedAlbum s" +
            " where a.id =:id and o.id =:userId")
    Optional <IndividualAlbum> findIndividualAlbumByOwnerAndReturnFullInformation(long id, long userId);



}
