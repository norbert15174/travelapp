package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.AccessForbiddenException;
import pl.travel.travelapp.exceptions.ObjectNotFoundException;
import pl.travel.travelapp.repositories.IndividualAlbumRepository;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;

import java.time.LocalDateTime;
import java.util.List;
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
        if ( !individualAlbum.isPresent() ) throw new ObjectNotFoundException();
        IndividualAlbum album = individualAlbum.get();
        if ( album.getOwner().getId().equals(user.getId()) || album.getSharedAlbum().stream().anyMatch(shared -> user.getId().equals(shared.getUserId())) || album.isPublic() ) {
            return new AlbumDTO().build(album);
        }
        throw new AccessForbiddenException();
    }

    @Transactional(readOnly = true)
    @Override
    public List <IndividualAlbumDTO> getIndividualAlbumsNews(Long userId , Pageable page) {
        return individualAlbumRepository.findIndividualAlbumNews(userId , page);
    }

    @Transactional(readOnly = true)
    @Override
    public List <IndividualAlbumDTO> getPublicAlbums() {
        return individualAlbumRepository.findPublicAlbums(PageRequest.of(0 , 20));
    }

    @Override
    public Optional <AlbumDTO> getAlbumById(Long id) {
        return individualAlbumRepository.findAlbumById(id);
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void fillDateInformation() {
        List <IndividualAlbum> fill = individualAlbumRepository.findAll();
        Integer i = 0;
        for (IndividualAlbum f : fill) {
            f.setDateTime(LocalDateTime.now().minusMinutes(i));
            individualAlbumRepository.save(f);
            i++;
        }
    }

}
