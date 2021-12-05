package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.DTO.PersonalDataDtoWithIndividualAlbumsDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.services.PersonalService;
import pl.travel.travelapp.specification.criteria.UserSearchCriteria;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class PersonalController {

    private PersonalService personalService;

    @Autowired
    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @PostMapping("/picture")
    public ResponseEntity <PersonalDataDTO> setUserProfilePicture(@RequestParam("file") MultipartFile file , Principal user) {
        return personalService.setPersonalDataProfilePicture(file , user);
    }


    @PutMapping("/profile")
    public ResponseEntity <PersonalDataDTO> updateProfile(@RequestBody PersonalDataDTO personalDataDTO , Principal principal) {
        return personalService.updatePersonalInformation(principal , personalDataDTO);
    }

    @GetMapping("/profile")
    public ResponseEntity <PersonalDataDTO> getProfileInformation(Principal principal) {
        return personalService.getUserProfile(principal);
    }

    @PostMapping("/background")
    public ResponseEntity <PersonalDataDTO> setUserBackgroundPicture(@RequestParam("file") MultipartFile file , Principal user) {
        return personalService.setPersonalDataBackgroundPicture(file , user);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity <PersonalDataDTO> getProfileInformation(@PathVariable("id") long id , Principal principal) {
        return personalService.getUserProfileInformation(id);
    }

    @GetMapping("/profile/full/{id}")
    public ResponseEntity <PersonalDataDtoWithIndividualAlbumsDTO> getProfileInformationWithAlbums(@PathVariable("id") long id , Principal principal) {
        return personalService.getUserProfileInformationWithAlbums(principal , id);
    }

    @GetMapping("/profile/basic")
    public ResponseEntity <PersonalInformationDTO> getBasicUserInformation(Principal principal) {
        return personalService.getBasicUserInformation(principal);
    }

    @GetMapping("/search")
    public ResponseEntity <List <PersonalInformationDTO>> getUsersBySearchCriteria(Principal principal , UserSearchCriteria criteria , @RequestParam(name = "page", defaultValue = "0") Integer page) {
        return personalService.getUsersBySearchCriteria(principal , criteria , page);
    }

    @GetMapping("/logged")
    public ResponseEntity isUserLogged() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
