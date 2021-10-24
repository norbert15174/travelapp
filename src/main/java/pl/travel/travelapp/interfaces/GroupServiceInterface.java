package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.*;

import java.security.Principal;
import java.util.List;

public interface GroupServiceInterface {
    ResponseEntity <GroupGetDTO> create(GroupCreateDTO group , Principal principal);

    ResponseEntity <GroupGetDTO> update(UpdateGroupDTO group , Principal principal);

    ResponseEntity <GroupGetDTO> getGroupById(Principal principal , Long groupId);

    ResponseEntity acceptGroupRequest(Principal principal , Long requestId);

    ResponseEntity <List <GroupRequestDTO>> getUserGroupRequests(Principal principal);

    ResponseEntity <List <GroupNotificationDTO>> getUserGroupNotification(Principal principal , Integer size , Integer page);

    ResponseEntity <GroupGetDTO> setGroupPhoto(Principal principal , MultipartFile file , Long groupId);
}
