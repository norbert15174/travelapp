package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.AccessForbiddenException;
import pl.travel.travelapp.exceptions.ObjectNotFoundException;
import pl.travel.travelapp.repositories.IndividualAlbumRepository;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;

import java.util.Optional;

@Service
public class IndividualAlbumQueryService implements IIndividualAlbumQueryService {

    private final IndividualAlbumRepository individualAlbumRepository;

    @Autowired
    public IndividualAlbumQueryService(IndividualAlbumRepository individualAlbumRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public AlbumDTO getFullAlbumInformation(PersonalData user , Long albumId) throws ObjectNotFoundException, AccessForbiddenException {
        Optional <IndividualAlbum> individualAlbum = individualAlbumRepository.findById(albumId);
        if ( individualAlbum.isEmpty() ) throw new ObjectNotFoundException();
        IndividualAlbum album = individualAlbum.get();
        if ( album.getOwner().getId().equals(user.getId()) || album.getSharedAlbum().stream().anyMatch(shared -> user.getId().equals(shared.getUserId())) ) {
            return new AlbumDTO().build(album);
        }
        throw new AccessForbiddenException();
    }
}
