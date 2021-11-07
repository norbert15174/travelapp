package pl.travel.travelapp.interfaces;

import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;
import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.entites.PersonalData;

import java.util.List;

public interface GroupAlbumHistoryInterface {
    void createGroupAlbum(GroupAlbum album, PersonalData user);

    void updateGroupAlbum(GroupAlbum album , PersonalData user);

    void setGroupAlbumMainPicture(GroupAlbum album , PersonalData user);

    void setGroupAlbumBackgroundPicture(GroupAlbum album , PersonalData user);

    void addNewPhoto(GroupAlbum album , PersonalData user);

    List <GroupAlbumHistoryDTO> getAlbumHistoryByGroupAlbumId(Long groupId , Integer page);

    void changeOwner(GroupAlbum album , PersonalData user);

    void addNewPhotos(GroupAlbum album , PersonalData user);
}
