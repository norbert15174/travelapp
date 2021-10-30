package pl.travel.travelapp.services.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.repositories.GroupMemberRequestRepository;
import pl.travel.travelapp.repositories.GroupRepository;
import pl.travel.travelapp.services.delete.interfaces.IGroupDeleteService;

@Service
public class GroupDeleteService implements IGroupDeleteService {

    private final GroupRepository groupRepository;
    private final GroupMemberRequestRepository groupMemberRequestRepository;

    @Autowired
    public GroupDeleteService(GroupRepository groupRepository , GroupMemberRequestRepository groupMemberRequestRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRequestRepository = groupMemberRequestRepository;
    }

    @Transactional
    @Override
    public void delete(UsersGroup group) {
        groupRepository.delete(group);
    }

    @Transactional
    @Override
    public void deleteMemberRequest(Long requestId) {
        groupMemberRequestRepository.deleteById(requestId);
    }
}
