package pl.travel.travelapp.services.delete;

import org.springframework.stereotype.Service;
import pl.travel.travelapp.entites.GroupPhotoTagged;
import pl.travel.travelapp.repositories.GroupPhotoTaggedRepository;
import pl.travel.travelapp.services.delete.interfaces.IGroupPhotoDeleteService;

import java.util.Set;

@Service
public class GroupPhotoDeleteService implements IGroupPhotoDeleteService {

    private final GroupPhotoTaggedRepository groupPhotoTaggedRepository;

    public GroupPhotoDeleteService(GroupPhotoTaggedRepository groupPhotoTaggedRepository) {
        this.groupPhotoTaggedRepository = groupPhotoTaggedRepository;
    }

    @Override
    public void untag(Set <GroupPhotoTagged> untag) {
        groupPhotoTaggedRepository.deleteAll(untag);
    }
}
