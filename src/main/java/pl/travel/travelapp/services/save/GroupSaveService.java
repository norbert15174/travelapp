package pl.travel.travelapp.services.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.repositories.GroupMemberRequestRepository;
import pl.travel.travelapp.repositories.GroupRepository;
import pl.travel.travelapp.services.save.interfaces.IGroupSaveService;

@Service
public class GroupSaveService implements IGroupSaveService {

    private final GroupRepository groupRepository;
    private final GroupMemberRequestRepository groupMemberRequestRepository;

    @Autowired
    public GroupSaveService(GroupRepository groupRepository , GroupMemberRequestRepository groupMemberRequestRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRequestRepository = groupMemberRequestRepository;
    }

    @Transactional
    @Override
    public UsersGroup create(UsersGroup group) {
        return groupRepository.save(group);
    }

    @Transactional
    @Override
    public UsersGroup update(UsersGroup groupToUpdate) {
        return groupRepository.save(groupToUpdate);
    }

    @Transactional
    @Override
    public void createMemberRequest(GroupMemberRequest request) {
        groupMemberRequestRepository.save(request);
    }

}
