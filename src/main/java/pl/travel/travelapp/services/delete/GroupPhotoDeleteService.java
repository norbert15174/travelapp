package pl.travel.travelapp.services.delete;

import org.springframework.stereotype.Service;
import pl.travel.travelapp.entites.GroupPhoto;
import pl.travel.travelapp.entites.GroupPhotoTagged;
import pl.travel.travelapp.repositories.GroupPhotoRepository;
import pl.travel.travelapp.repositories.GroupPhotoTaggedRepository;
import pl.travel.travelapp.services.delete.interfaces.IGroupPhotoDeleteService;

import java.util.Set;

@Service
public class GroupPhotoDeleteService implements IGroupPhotoDeleteService {

    private final GroupPhotoTaggedRepository groupPhotoTaggedRepository;
    private final GroupPhotoRepository groupPhotoRepository;

    public GroupPhotoDeleteService(GroupPhotoTaggedRepository groupPhotoTaggedRepository , GroupPhotoRepository groupPhotoRepository) {
        this.groupPhotoTaggedRepository = groupPhotoTaggedRepository;
        this.groupPhotoRepository = groupPhotoRepository;
    }

    @Override
    public void untag(Set <GroupPhotoTagged> untag) {
        groupPhotoTaggedRepository.deleteAll(untag);
    }

    @Override
    public void delete(GroupPhoto photo) {
        groupPhotoRepository.delete(photo);
    }
}
