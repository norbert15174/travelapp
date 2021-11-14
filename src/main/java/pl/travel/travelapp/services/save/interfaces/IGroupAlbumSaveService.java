package pl.travel.travelapp.services.save.interfaces;

import pl.travel.travelapp.entites.GroupAlbum;

import java.util.Set;

public interface IGroupAlbumSaveService {
    GroupAlbum save(GroupAlbum album);

    void saveAll(Set<GroupAlbum> groupAlbumsToChange);
}
