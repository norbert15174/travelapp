package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.AccessForbiddenException;
import pl.travel.travelapp.exceptions.ObjectNotFoundException;
import pl.travel.travelapp.repositories.IndividualAlbumRepository;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;
import pl.travel.travelapp.specification.AlbumSpecification;
import pl.travel.travelapp.specification.criteria.AlbumSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class IndividualAlbumQueryService implements IIndividualAlbumQueryService {

    private final IndividualAlbumRepository individualAlbumRepository;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    public IndividualAlbumQueryService(IndividualAlbumRepository individualAlbumRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
    }

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

    @Override
    public List <IndividualAlbumDTO> getIndividualAlbumsNews(Long userId , Pageable page) {
        return individualAlbumRepository.findIndividualAlbumNews(userId , page);
    }

    @Override
    public List <IndividualAlbumDTO> getPublicAlbums() {
        return individualAlbumRepository.findPublicAlbums(PageRequest.of(0 , 20));
    }

    @Override
    public Optional <AlbumDTO> getAlbumById(Long id) {
        return individualAlbumRepository.findAlbumById(id);
    }

    @Override
    public List <BasicIndividualAlbumDTO> getAlbumsByCriteria(AlbumSearchCriteria criteria , Integer page) {
        Specification <IndividualAlbum> filter = new AlbumSpecification().getFilter(criteria);
        Page <IndividualAlbum> albums = individualAlbumRepository.findAll(filter , PageRequest.of(page , PAGE_SIZE));
        return albums.get()
                .map(BasicIndividualAlbumDTO::new)
                .collect(Collectors.toList());
    }

}
