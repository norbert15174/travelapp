package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.travel.travelapp.DTO.groups.GroupCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.DTO.groups.UpdateGroupDTO;
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

}
