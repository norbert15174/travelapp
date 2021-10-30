package pl.travel.travelapp.services.query.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.GroupRepository;

import java.util.List;
import java.util.Set;

public interface IGroupQueryService {

    UsersGroup getGroupById(Long groupId) throws NotFoundException;

    GroupGetDTO getGroupDTOById(Long groupId);

    GroupMemberRequest getGroupRequestById(Long requestId) throws NotFoundException;

    Set<GroupRequestDTO> getUserGroupRequests(PersonalData user);

    Set <GroupMemberRequest> getUserGroupRequestsEntity(PersonalData user);

    GroupMemberRequest getGroupMemberRequestByGroupIdAndUserId(Long groupId , Long id);

    List<GroupGetDTO> getUserGroups(Long id);
}
