package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.builders.IndividualAlbumFullInformationBuilder;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.services.IndividualAlbumService;
import pl.travel.travelapp.specification.criteria.AlbumSearchCriteria;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/albums")
@CrossOrigin(origins = "*")
public class IndividualAlbumController {

    private IndividualAlbumInterface individualAlbumService;

    public IndividualAlbumController(IndividualAlbumService individualAlbumService) {
        this.individualAlbumService = individualAlbumService;
    }

    @PostMapping("/add")
    public ResponseEntity <IndividualAlbumDTO> addNewAlbum(@RequestBody IndividualAlbumDTO album , Principal principal) {
        return individualAlbumService.addNewAlbum(principal , album);
    }

    @DeleteMapping
    public ResponseEntity deleteAlbum(@RequestParam long id , Principal principal) {
        return individualAlbumService.deleteAlbum(principal , id);
    }

    @GetMapping
    public ResponseEntity <List <IndividualAlbumDTO>> findUserAlbums(Principal principal) {
        return individualAlbumService.findAllUserAlbums(principal);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity <List <IndividualAlbumDTO>> findUserAlbums(@PathVariable("id") long id) {
        return individualAlbumService.findAlbumsByUserId(id);
    }

    @GetMapping("/name")
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumByName(@RequestParam String album , @RequestParam(defaultValue = "0") int page) {
        return individualAlbumService.findAlbumsByName(album , page);
    }

    @PostMapping("/mainphoto/{id}")
    public ResponseEntity <IndividualAlbumDTO> setMainPhoto(Principal principal , @RequestParam("file") MultipartFile file , @PathVariable("id") long id) {
        return individualAlbumService.setMainPhotoToIndividualAlbum(principal , file , id);
    }

    @GetMapping("/full/{id}")
    public ResponseEntity <List <IndividualAlbumFullInformationBuilder>> findFullUserAlbum(@PathVariable("id") long id , Principal principal) {
        return individualAlbumService.findFullAlbumsByUserId(id , principal);
    }

    @GetMapping("/shared")
    public ResponseEntity <List <AlbumDTO>> getAvailableAlbums(Principal principal) {
        return individualAlbumService.getAvailableAlbums(principal);
    }

    @DeleteMapping("/shared")
    public ResponseEntity deleteSharedAlbum(Principal principal , @RequestBody List <Long> sharedIds) {
        return individualAlbumService.deleteShared(principal , sharedIds);
    }

    @PostMapping("/shared/{id}")
    public ResponseEntity addSharedAlbum(Principal principal , @RequestBody List <Long> sharedIds , @PathVariable("id") Long id) {
        return individualAlbumService.addShared(principal , sharedIds , id);
    }

    @GetMapping("/{id}")
    public ResponseEntity <AlbumDTO> getAvailableAlbums(Principal principal , @PathVariable("id") Long id) {
        return individualAlbumService.getAlbumFullInformation(principal , id);
    }

    @PutMapping("/{id}")
    public ResponseEntity <BasicIndividualAlbumDTO> modifyAlbum(Principal principal , @RequestBody BasicIndividualAlbumDTO basicIndividualAlbumDTO , @PathVariable(name = "id") Long id) {
        return individualAlbumService.modifyAlbum(principal , basicIndividualAlbumDTO , id);
    }

    @GetMapping("/search")
    public ResponseEntity <List <BasicIndividualAlbumDTO>> getAlbumsBySearchCriteria(Principal principal , AlbumSearchCriteria criteria , @RequestParam(name = "page", defaultValue = "0") Integer page) {
        return individualAlbumService.getAlbumsByCriteria(principal , criteria , page);
    }


}
