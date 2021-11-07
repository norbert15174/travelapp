package pl.travel.travelapp.services.save.interfaces;

import pl.travel.travelapp.DTO.groups.GroupPhotoAlbumEnterDTO;
import pl.travel.travelapp.entites.GroupPhoto;
import pl.travel.travelapp.entites.GroupPhotoComments;
import pl.travel.travelapp.entites.GroupPhotoTagged;

import java.util.Set;

public interface IGroupPhotoSaveService {
    Set <GroupPhotoAlbumEnterDTO> saveAll(Set<GroupPhoto> groupPhotos);

    GroupPhotoAlbumEnterDTO save(GroupPhoto groupPhotos);

    GroupPhotoTagged saveTagged(GroupPhotoTagged tagged);

    void saveComment(GroupPhotoComments comment);
}
