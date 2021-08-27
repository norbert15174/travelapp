package pl.travel.travelapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.travel.travelapp.entites.Country;
import pl.travel.travelapp.services.PersonalService;

import java.util.List;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class DataController {

    private final PersonalService personalService;

    public DataController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping("/countries")
    public ResponseEntity <List <Country>> getCountries(){
        return personalService.getCountries();
    }

}
