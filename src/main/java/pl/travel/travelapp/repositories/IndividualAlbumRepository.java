package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.models.IndividualAlbum;


import java.util.List;

@Repository
public interface IndividualAlbumRepository extends JpaRepository<IndividualAlbum,Long> {
    @Query("select a from IndividualAlbum a left join fetch a.owner o where o.id =:id and a.isPublic = true")
    List <IndividualAlbum> findUserPublicAlbumsByUserId(long id);
    @Query("select a from IndividualAlbum a left join fetch a.owner o where a.name=:name and a.isPublic = true")
    List <IndividualAlbum> findUserAlbumsByAlbumName(String name, Pageable pageable);
}
