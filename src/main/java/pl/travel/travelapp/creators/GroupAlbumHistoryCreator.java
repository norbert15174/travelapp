package pl.travel.travelapp.creators;

import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.entites.GroupAlbumHistory;
import pl.travel.travelapp.entites.enums.GroupAlbumHistoryStatus;

import java.time.LocalDateTime;

public class GroupAlbumHistoryCreator {

    public static GroupAlbumHistory createNewGroup(GroupAlbum album) {
        GroupAlbumHistory groupAlbumHistory = new GroupAlbumHistory();
        groupAlbumHistory.setAlbum(album);
        groupAlbumHistory.setDateTime(LocalDateTime.now());
        groupAlbumHistory.setUser(album.getOwner());
        groupAlbumHistory.setStatus(GroupAlbumHistoryStatus.CREATE_ALBUM);
        return groupAlbumHistory;
    }
}
