package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.entites.IndividualAlbum;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndividualAlbumRepository extends JpaRepository <IndividualAlbum, Long>, JpaSpecificationExecutor <IndividualAlbum> {
    @Query("select a from IndividualAlbum a left join fetch a.owner o where o.id =:id and a.isPublic = true")
    List <IndividualAlbum> findUserPublicAlbumsByUserId(long id);

    @Query("select a from IndividualAlbum a left join fetch a.owner o left join fetch a.photos p where o.id =:id")
    List <IndividualAlbum> findFullUserPublicAlbumsByUserId(long id);

    @Query("select a from IndividualAlbum a left join fetch a.owner o where a.name=:name and a.isPublic = true")
    List <IndividualAlbum> findUserAlbumsByAlbumName(String name , Pageable pageable);

    @Query("select a from IndividualAlbum  a left join fetch a.owner o left join fetch a.sharedAlbum s where a.id =:id and (a.owner.id =:userId or s.userId =:userId or a.isPublic = true)")
    Optional <IndividualAlbum> findIndividualAlbumById(long id , long userId);

    @Query("select a from IndividualAlbum a left join fetch a.owner o where o.id =:id")
    List <IndividualAlbum> findUserAlbumsByUserId(long id);

    //TODO CHECK IF WORK WELL
    @Query("select a from IndividualAlbum a left join fetch a.owner o left join fetch a.sharedAlbum s where a.id =:id and (a.owner.id =:userId or s.userId =:userId or a.isPublic = true)")
    Optional <IndividualAlbum> findIndividualAlbumByIdAndReturnFullInformation(long id , long userId);

    @Query("select a from IndividualAlbum a left join fetch a.owner o left join fetch a.sharedAlbum s where a.id =:id and (o.id =:userId)")
    Optional <IndividualAlbum> findIndividualAlbumByOwner(long id , long userId);

    @Query("select a from IndividualAlbum a" +
            " left join fetch a.owner o" +
            " left join fetch a.sharedAlbum s" +
            " where a.id =:id and o.id =:userId")
    Optional <IndividualAlbum> findIndividualAlbumByOwnerAndReturnFullInformation(long id , long userId);

    @Query("select distinct new pl.travel.travelapp.DTO.IndividualAlbumDTO(a) from IndividualAlbum a" +
            " left join a.owner o" +
            " left join a.sharedAlbum s" +
            " where a.id = :userId or o.id =:userId or a.isPublic = true" +
            " order by a.dateTime desc")
    List <IndividualAlbumDTO> findIndividualAlbumNews(Long userId , Pageable page);

    @Query("select new pl.travel.travelapp.DTO.IndividualAlbumDTO(a) from IndividualAlbum a" +
            " left join a.owner o" +
            " left join a.sharedAlbum s" +
            " where a.isPublic = true" +
            " order by a.dateTime desc")
    List <IndividualAlbumDTO> findPublicAlbums(Pageable page);

    @Query("select new pl.travel.travelapp.DTO.albums.AlbumDTO(a) from IndividualAlbum a" +
            " left join a.owner o" +
            " left join a.sharedAlbum s" +
            " where a.isPublic = true and a.id = :id" +
            " order by a.dateTime desc")
    Optional <AlbumDTO> findAlbumById(Long id);
}
