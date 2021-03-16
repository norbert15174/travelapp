package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.services.PersonalService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class PersonalController {

    private PersonalService personalService;
    @Autowired
    public PersonalController(PersonalService personalService) {
        this.personalService = personalService;
    }




}
