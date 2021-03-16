package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.mappers.PersonalDataObjectMapperClass;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.PersonalDescriptionRepository;
import pl.travel.travelapp.repositories.UserRepository;

@Service
public class PersonalService {

    private PersonalDataRepository personalDataRepository;
    private PersonalDescriptionRepository personalDescriptionRepository;
    private CountryRepository countryRepository;
    private UserRepository userRepository;
    @Autowired
    public PersonalService(PersonalDataRepository personalDataRepository, PersonalDescriptionRepository personalDescriptionRepository, CountryRepository countryRepository, UserRepository userRepository) {
        this.personalDataRepository = personalDataRepository;
        this.personalDescriptionRepository = personalDescriptionRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<PersonalDataDTO> getPersonalInformation(UserDetails user){
        PersonalData userData = getPersonalInformation(user.getUsername());
        if(userData == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData),HttpStatus.OK);
    }

    private PersonalData getPersonalInformation(String username){
        try {
            return userRepository.findPersonalDataByUser(username).getPersonalData();
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }


}
