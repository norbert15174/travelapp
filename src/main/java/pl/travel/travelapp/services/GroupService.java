package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.UsersGroup;
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
