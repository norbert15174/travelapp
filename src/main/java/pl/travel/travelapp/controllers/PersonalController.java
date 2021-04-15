package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.services.PersonalService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class PersonalController {

    private PersonalService personalService;
    @Autowired
    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @PostMapping("/picture")
    public ResponseEntity<PersonalDataDTO> setUserProfilePicture(@RequestParam("file")  MultipartFile file, Principal user){
        return personalService.setPersonalDataProfilePicture(file,user);
    }



}
