package pl.travel.travelapp.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;
import pl.travel.travelapp.creators.GroupAlbumHistoryCreator;
import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.entites.GroupAlbumHistory;
import pl.travel.travelapp.interfaces.GroupAlbumHistoryInterface;
import pl.travel.travelapp.repositories.GroupAlbumHistoryRepository;

import java.util.List;

@Service
public class GroupAlbumHistoryService implements GroupAlbumHistoryInterface {

    private final GroupAlbumHistoryRepository groupAlbumHistoryRepository;

    public GroupAlbumHistoryService(GroupAlbumHistoryRepository groupAlbumHistoryRepository) {
        this.groupAlbumHistoryRepository = groupAlbumHistoryRepository;
    }

    @Transactional
    @Override
    public void createGroupAlbum(GroupAlbum album) {
        create(GroupAlbumHistoryCreator.createNewGroup(album));
    }

    @Transactional
    @Override
    public List <GroupAlbumHistoryDTO> getAlbumHistoryByGroupAlbumId(Long groupAlbumId , Integer page) {
        return groupAlbumHistoryRepository.getAlbumHistoryByGroupAlbumId(groupAlbumId , PageRequest.of(page , 10));
    }

    private void create(GroupAlbumHistory history) {
        groupAlbumHistoryRepository.save(history);
    }


}
