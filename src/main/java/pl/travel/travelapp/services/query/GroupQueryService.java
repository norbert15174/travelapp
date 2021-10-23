package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.GroupMemberRequestRepository;
import pl.travel.travelapp.repositories.GroupRepository;
import pl.travel.travelapp.services.query.interfaces.IGroupQueryService;

import java.util.Set;

@Service
public class GroupQueryService implements IGroupQueryService {

    private final GroupRepository groupRepository;
    private final GroupMemberRequestRepository groupMemberRequestRepository;

    @Autowired
    public GroupQueryService(GroupRepository groupRepository , GroupMemberRequestRepository groupMemberRequestRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRequestRepository = groupMemberRequestRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UsersGroup getGroupById(Long groupId) throws NotFoundException {
        return groupRepository.findGroupById(groupId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public GroupGetDTO getGroupDTOById(Long groupId) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public GroupMemberRequest getGroupRequestById(Long requestId) throws NotFoundException {
        return groupMemberRequestRepository.findRequestById(requestId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Set <GroupRequestDTO> getUserGroupRequests(PersonalData user) {
        return groupMemberRequestRepository.findUserGroupRequests(user.getId());
    }

}
