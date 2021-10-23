package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.DTO.groups.UpdateGroupDTO;

import java.security.Principal;
import java.util.List;

public interface GroupServiceInterface {
    ResponseEntity <GroupGetDTO> create(GroupCreateDTO group , Principal principal);

    ResponseEntity <GroupGetDTO> update(UpdateGroupDTO group , Principal principal);

    ResponseEntity <GroupGetDTO> getGroupById(Principal principal , Long groupId);

    ResponseEntity acceptGroupRequest(Principal principal , Long requestId);

    ResponseEntity<List <GroupRequestDTO>> getUserGroupRequests(Principal principal);
}
