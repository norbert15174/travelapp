package pl.travel.travelapp.services.query.interfaces;

import org.springframework.data.domain.Pageable;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.AccessForbiddenException;
import pl.travel.travelapp.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IIndividualAlbumQueryService {

    AlbumDTO getFullAlbumInformation(PersonalData user , Long albumId) throws ObjectNotFoundException, AccessForbiddenException;

    List <IndividualAlbumDTO> getIndividualAlbumsNews(Long userId , Pageable page);

    List <IndividualAlbumDTO> getPublicAlbums();

    Optional <AlbumDTO> getAlbumById(Long id);
}
