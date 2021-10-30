package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.*;
import pl.travel.travelapp.interfaces.GroupServiceInterface;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/group")
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupServiceInterface groupService;

    @Autowired
    public GroupController(GroupServiceInterface groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity <GroupGetDTO> create(@RequestBody GroupCreateDTO group , Principal principal) {
        return groupService.create(group , principal);
    }

    @PutMapping("/user/{requestId}")
    public ResponseEntity <GroupGetDTO> acceptRequest(@PathVariable(name = "requestId") Long requestId , Principal principal) {
        return groupService.acceptGroupRequest(principal , requestId);
    }

    @PutMapping
    public ResponseEntity <GroupGetDTO> update(@RequestBody UpdateGroupDTO group , Principal principal) {
        return groupService.update(group , principal);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity <GroupGetDTO> getGroupById(@PathVariable(name = "groupId") Long groupId , Principal principal) {
        return groupService.getGroupById(principal , groupId);
    }

    @GetMapping("/user")
    public ResponseEntity <List <GroupRequestDTO>> getUserGroupRequests(Principal principal) {
        return groupService.getUserGroupRequests(principal);
    }

    @GetMapping("/user/notification")
    public ResponseEntity <List <GroupNotificationDTO>> getUserNotification(Principal principal ,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page ,
                                                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return groupService.getUserGroupNotification(principal , page , size);
    }

    @PostMapping("/photo/{groupId}")
    public ResponseEntity <GroupGetDTO> setMainPhoto(Principal principal , @RequestParam("file") MultipartFile file , @PathVariable("groupId") Long groupId) {
        return groupService.setGroupPhoto(principal , file , groupId);
    }

    @PutMapping("/owner/{groupId}")
    public ResponseEntity <GroupGetDTO> changeOwner(Principal principal , @PathVariable("groupId") Long groupId , @RequestParam(name = "userId") Long userId) {
        return groupService.changeOwner(principal , groupId , userId);
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity leaveGroup(Principal principal , @PathVariable(name = "groupId") Long groupId) {
        return groupService.leaveGroup(principal , groupId);
    }

    @DeleteMapping("/user/{requestId}")
    public ResponseEntity <GroupGetDTO> deleteGroupRequest(@PathVariable(name = "requestId") Long requestId , Principal principal) {
        return groupService.deleteGroupRequest(principal , requestId);
    }

    @DeleteMapping("/member/{groupId}")
    public ResponseEntity <GroupGetDTO> removeMemberFromGroup(Principal principal , @PathVariable(name = "groupId") Long groupId , @RequestParam(name = "userId") Long userId) {
        return groupService.removeMemberFromGroup(principal , groupId , userId);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity <GroupGetDTO> deleteGroup(Principal principal , @PathVariable(name = "groupId") Long groupId) {
        return groupService.deleteGroup(principal , groupId);
    }

    @GetMapping
    public ResponseEntity<List<GroupGetDTO>> getUserGroups(Principal principal){
        return groupService.getUserGroups(principal);
    }
}
