package pl.travel.travelapp.interfaces;

import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;
import pl.travel.travelapp.entites.GroupAlbum;

import java.util.List;

public interface GroupAlbumHistoryInterface {
    void createGroupAlbum(GroupAlbum album);

    List <GroupAlbumHistoryDTO> getAlbumHistoryByGroupAlbumId(Long groupId , Integer page);
}
