package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.SharedAlbum;

import java.util.List;
import java.util.Set;

@Repository
public interface SharedAlbumRepository extends JpaRepository <SharedAlbum, Long> {
    @Query("select sa from SharedAlbum sa where sa.userId = :id")
    List <SharedAlbum> findAvailableAlbums(@Param("id") long id);

    @Query("select sa from SharedAlbum sa where sa.userId = :id")
    List <SharedAlbum> findAvailableAlbumsPage(@Param("id") long id , Pageable page);

    @Query("select sa from SharedAlbum sa inner join fetch sa.individualAlbum ia inner join ia.owner o where (sa.userId = :userId and o.id = :ownerId) or (sa.userId = :ownerId and o.id = :userId)")
    Set <SharedAlbum> findAllUserSharedAlbumsBySharedUserId(long userId , long ownerId);

    @Query("select sh from SharedAlbum sh where sh.id in :ids")
    Set <SharedAlbum> findAllByIds(Set <Long> ids);
}
