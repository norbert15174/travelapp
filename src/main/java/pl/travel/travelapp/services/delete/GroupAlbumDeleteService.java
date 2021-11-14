package pl.travel.travelapp.services.delete;

import org.springframework.stereotype.Service;
import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.repositories.GroupAlbumRepository;
import pl.travel.travelapp.services.delete.interfaces.IGroupAlbumDeleteService;

@Service
public class GroupAlbumDeleteService implements IGroupAlbumDeleteService {

    private final GroupAlbumRepository groupAlbumRepository;

    public GroupAlbumDeleteService(GroupAlbumRepository groupAlbumRepository) {
        this.groupAlbumRepository = groupAlbumRepository;
    }

    @Override
    public void delete(GroupAlbum groupAlbum) {
        groupAlbumRepository.delete(groupAlbum);
    }
}
