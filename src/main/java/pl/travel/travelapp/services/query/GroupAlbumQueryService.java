package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO;
import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.GroupAlbumRepository;
import pl.travel.travelapp.services.query.interfaces.IGroupAlbumQueryService;

@Service
public class GroupAlbumQueryService implements IGroupAlbumQueryService {

    private final GroupAlbumRepository groupAlbumRepository;

@Autowired
    public GroupAlbumQueryService(GroupAlbumRepository groupAlbumRepository) {
        this.groupAlbumRepository = groupAlbumRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public GroupAlbum getGroupAlbumById(Long groupAlbumId) throws NotFoundException {
        return groupAlbumRepository.findById(groupAlbumId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public GroupAlbumFullDTO getGroupAlbumByIdWithPhotos(Long groupAlbumId) {
        return groupAlbumRepository.findGroupAlbumByIdWithPhotos(groupAlbumId);
    }

}
