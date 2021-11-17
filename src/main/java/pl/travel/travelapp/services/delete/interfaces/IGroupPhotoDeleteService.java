package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.entites.GroupPhoto;
import pl.travel.travelapp.entites.GroupPhotoTagged;

import java.util.Set;

public interface IGroupPhotoDeleteService {
    void untag(Set<GroupPhotoTagged> untag);

    void delete(GroupPhoto photo);

    void deleteAll(Set<GroupPhoto> photos);
}
