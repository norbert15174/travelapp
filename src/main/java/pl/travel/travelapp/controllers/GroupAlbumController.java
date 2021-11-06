package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.travel.travelapp.DTO.groups.GroupAlbumCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;
import pl.travel.travelapp.interfaces.GroupAlbumInterface;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/group/albums")
@CrossOrigin(origins = "*")
public class GroupAlbumController {

    private final GroupAlbumInterface groupAlbumService;

    public GroupAlbumController(GroupAlbumInterface groupAlbumService) {
        this.groupAlbumService = groupAlbumService;
    }

    @PostMapping("/{groupId}")
    public ResponseEntity <GroupAlbumDTO> create(Principal principal , @RequestBody GroupAlbumCreateDTO model , @PathVariable(name = "groupId") Long groupId) {
        return groupAlbumService.create(principal , model , groupId);
    }

    @GetMapping("/history/{groupAlbumId}")
    public ResponseEntity <List <GroupAlbumHistoryDTO>> getAlbumHistoryByGroupAlbumId(Principal principal , @PathVariable(name = "groupAlbumId") Long groupAlbumId , @RequestParam Integer page) {
        return groupAlbumService.getAlbumHistoryByGroupAlbumId(principal , groupAlbumId , page);
    }


}
