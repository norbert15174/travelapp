package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.AccessForbiddenException;
import pl.travel.travelapp.exceptions.ObjectNotFoundException;

import java.security.Principal;

public interface IIndividualAlbumQueryService {

    AlbumDTO getFullAlbumInformation(PersonalData user, Long albumId) throws ObjectNotFoundException, AccessForbiddenException;

}
