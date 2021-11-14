package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.*;
import pl.travel.travelapp.interfaces.GroupAlbumInterface;
import pl.travel.travelapp.interfaces.GroupPhotoInterface;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/group/albums")
@CrossOrigin(origins = "*")
public class GroupAlbumController {

    private final GroupAlbumInterface groupAlbumService;
    private final GroupPhotoInterface groupPhotoService;

    public GroupAlbumController(GroupAlbumInterface groupAlbumService , GroupPhotoInterface groupPhotoService) {
        this.groupAlbumService = groupAlbumService;
        this.groupPhotoService = groupPhotoService;
    }

    @PostMapping("/{groupId}")
    public ResponseEntity <GroupAlbumDTO> create(Principal principal , @RequestBody GroupAlbumCreateDTO model , @PathVariable(name = "groupId") Long groupId) {
        return groupAlbumService.create(principal , model , groupId);
    }

    @PutMapping("/{groupAlbumId }")
    public ResponseEntity <GroupAlbumDTO> update(Principal principal , @RequestBody GroupAlbumCreateDTO model , @PathVariable(name = "groupAlbumId ") Long groupAlbumId) {
        return groupAlbumService.update(principal , model , groupAlbumId);
    }

    @GetMapping("/history/{groupAlbumId}")
    public ResponseEntity <List <GroupAlbumHistoryDTO>> getAlbumHistoryByGroupAlbumId(Principal principal , @PathVariable(name = "groupAlbumId") Long groupAlbumId , @RequestParam Integer page) {
        return groupAlbumService.getAlbumHistoryByGroupAlbumId(principal , groupAlbumId , page);
    }

    @PutMapping("/main/{groupAlbumId}")
    public ResponseEntity <GroupAlbumDTO> setMainAlbumPhoto(Principal principal , @RequestParam("file") MultipartFile file , @PathVariable("groupAlbumId") Long groupAlbumId) {
        return groupAlbumService.setMainAlbumPhoto(principal , file , groupAlbumId);
    }

    @PutMapping("/background/{groupAlbumId}")
    public ResponseEntity <GroupAlbumDTO> setBackgroundAlbumPhoto(Principal principal , @RequestParam("file") MultipartFile file , @PathVariable("groupAlbumId") Long groupAlbumId) {
        return groupAlbumService.setBackgroundAlbumPhoto(principal , file , groupAlbumId);
    }

    @GetMapping("/{groupAlbumId}/photos")
    public ResponseEntity <List <GroupPhotoDTO>> getGroupPhotoByGroupAlbumId(Principal principal , @PathVariable("groupAlbumId") Long groupAlbumId , @RequestParam Integer page) {
        return groupPhotoService.getGroupPhotoByGroupAlbumId(principal , groupAlbumId , page);
    }

    @GetMapping("/{groupAlbumId}/full")
    public ResponseEntity <GroupAlbumFullDTO> getGroupAlbumFullInformation(Principal principal , @PathVariable("groupAlbumId") Long groupAlbumId) {
        return groupAlbumService.getGroupAlbumFullInformation(principal , groupAlbumId);
    }

    @DeleteMapping("/{groupAlbumId}")
    public ResponseEntity deleteAlbumById(Principal principal , @PathVariable("groupAlbumId") Long groupAlbumId) {
        return groupAlbumService.deleteAlbum(principal , groupAlbumId);
    }

}
