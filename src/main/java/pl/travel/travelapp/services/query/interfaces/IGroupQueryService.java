package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.GroupRepository;

import java.util.Set;

public interface IGroupQueryService {

    UsersGroup getGroupById(Long groupId) throws NotFoundException;

    GroupGetDTO getGroupDTOById(Long groupId);

    GroupMemberRequest getGroupRequestById(Long requestId) throws NotFoundException;

    Set<GroupRequestDTO> getUserGroupRequests(PersonalData user);
}
