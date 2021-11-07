package pl.travel.travelapp.creators;

import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.entites.GroupAlbumHistory;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.enums.GroupAlbumHistoryStatus;

import java.time.LocalDateTime;

public class GroupAlbumHistoryCreator {

    public static GroupAlbumHistory createNewGroup(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.CREATE_ALBUM);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }

    public static GroupAlbumHistory setGroupAlbumMainPicture(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.CHANGED_MAIN_PHOTO);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }

    public static GroupAlbumHistory setGroupAlbumBackgroundPicture(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.CHANGED_BACKGROUND_PHOTO);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }

    public static GroupAlbumHistory updateGroupAlbum(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.CHANGED_ALBUM);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }

    public static GroupAlbumHistory changeOwner(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.NEW_OWNER);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }

    private static GroupAlbumHistory create(GroupAlbum album , GroupAlbumHistoryStatus status) {
        GroupAlbumHistory groupAlbumHistory = new GroupAlbumHistory();
        groupAlbumHistory.setAlbum(album);
        groupAlbumHistory.setDateTime(LocalDateTime.now());
        groupAlbumHistory.setStatus(status);
        return groupAlbumHistory;
    }

    public static GroupAlbumHistory addNewPhoto(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.NEW_PHOTO);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }

    public static GroupAlbumHistory addNewPhotos(GroupAlbum album , PersonalData user) {
        GroupAlbumHistory groupAlbumHistory = create(album , GroupAlbumHistoryStatus.NEW_PHOTOS);
        groupAlbumHistory.setUser(user);
        return groupAlbumHistory;
    }
}
