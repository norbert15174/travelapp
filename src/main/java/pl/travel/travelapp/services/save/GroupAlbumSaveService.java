package pl.travel.travelapp.services.save;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.repositories.GroupAlbumRepository;
import pl.travel.travelapp.services.save.interfaces.IGroupAlbumSaveService;

@Service
public class GroupAlbumSaveService implements IGroupAlbumSaveService {

    private final GroupAlbumRepository groupAlbumRepository;

    public GroupAlbumSaveService(GroupAlbumRepository groupAlbumRepository) {
        this.groupAlbumRepository = groupAlbumRepository;
    }

    @Transactional
    @Override
    public GroupAlbum save(GroupAlbum album) {
        return groupAlbumRepository.save(album);
    }

}
