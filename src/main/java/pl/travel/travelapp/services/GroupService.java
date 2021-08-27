package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.repositories.GroupRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

@Service
public class GroupService {
    private PersonalDataRepository personalDataRepository;
    private GroupRepository groupRepository;

    @Autowired
    public GroupService(PersonalDataRepository personalDataRepository , GroupRepository groupRepository) {
        this.personalDataRepository = personalDataRepository;
        this.groupRepository = groupRepository;
    }


}
