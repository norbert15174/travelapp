package pl.travel.travelapp.services;


import com.google.cloud.storage.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.mappers.PersonalDataObjectMapperClass;
import pl.travel.travelapp.models.Country;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.PersonalDescription;
import pl.travel.travelapp.models.User;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.PersonalDescriptionRepository;
import pl.travel.travelapp.repositories.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalService {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

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

    public ResponseEntity<PersonalDataDTO> getUserProfile(UserDetails user) {
        try {
            PersonalData userData = userData = getPersonalInformation(user.getUsername());
            return new ResponseEntity<>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Transactional
    public ResponseEntity<PersonalDataDTO> updatePersonalInformation(UserDetails user, PersonalDataDTO userUpdate) {
        try {
            PersonalData userProfile = fillPersonalInformation(getPersonalInformation(user.getUsername()), userUpdate);
            try {
                personalDataRepository.save(userProfile);
                return new ResponseEntity<>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userProfile), HttpStatus.OK);
            } catch (Exception e) {
                System.err.println(e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    private PersonalData fillPersonalInformation(PersonalData userProfile,PersonalDataDTO userUpdate){
        userProfile.setNationality(userUpdate.getNationality());
        userProfile.setPhoneNumber(userUpdate.getPhoneNumber());
        userProfile.setBirthDate(userUpdate.getBirthday());
        userProfile.setSurName(userUpdate.getSurName());
        userProfile.setFirstName(userUpdate.getFirstName());
        userProfile.setProfilePicture(userUpdate.getProfilePicture());
        userProfile.setBackgroundPicture(userUpdate.getBackgroundPicture());
        PersonalDescription personalDescription = userProfile.getPersonalDescription();
        personalDescription.setVisitedCountries(userUpdate.getPersonalDescription().getVisitedCountries());
        personalDescription.setInterest(userUpdate.getPersonalDescription().getInterest());
        personalDescription.setAbout(userUpdate.getPersonalDescription().getAbout());
        userProfile.setPersonalDescription(personalDescription);
        return userProfile;
    }




    //return personal information (PersonalData and PersonalDescription as field)
    private PersonalData getPersonalInformation(String username) throws NotFoundException {
        try {
            return userRepository.findPersonalDataByUser(username).getPersonalData();
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    @Transactional
    public ResponseEntity<PersonalDataDTO> setPersonalDataProfilePicture(MultipartFile file, UserDetails user) {
        PersonalData userData = userRepository.findPersonalDataByUser(user.getUsername()).getPersonalData();
        String path = "user/" + user.getUsername() + "/picture/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket, path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo, file.getBytes());
            if (userData.getProfilePicture() != null) {
                storage.delete(BlobId.of("telephoners", userData.getProfilePicture().split(bucket + "/")[1]));
            }
            userData.setProfilePicture(url + path);
            personalDataRepository.save(userData);
            return new ResponseEntity<>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(personalDataRepository.findPersonalDataById(userData.getId())), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @Transactional
    public ResponseEntity<PersonalDataDTO> setPersonalDataBackgroundPicture(MultipartFile file, UserDetails user) {
        PersonalData userData = userRepository.findPersonalDataByUser(user.getUsername()).getPersonalData();
        String path = "user/" + user.getUsername() + "/background/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket, path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo, file.getBytes());
            if (userData.getBackgroundPicture() != null) {
                storage.delete(BlobId.of("telephoners", userData.getBackgroundPicture().split(bucket + "/")[1]));
            }
            userData.setProfilePicture(url + path);
            personalDataRepository.save(userData);
            return new ResponseEntity<>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(personalDataRepository.findPersonalDataById(userData.getId())), HttpStatus.OK);
        } catch (IOException e) {
            System.err.println(e);
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }


    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void test(){
//        PersonalDataDTO personalDataDTO = new PersonalDataDTO();
//        personalDataDTO.setBackgroundPicture("https://storage.googleapis.com/telephoners/20210216_225118.jpg");
//        personalDataDTO.setSurName("Faron");
//        personalDataDTO.setProfilePicture("https://storage.googleapis.com/telephoners/20210216_225118.jpg");
//        personalDataDTO.setPhoneNumber(511422350);
//        personalDataDTO.setNationality(countryRepository.findFirstByCountry("Poland").get());
//        personalDataDTO.setFirstName("Norbert");
//        personalDataDTO.setBirthday(LocalDate.now());
//        PersonalDescription personalDescription = new PersonalDescription();
//        personalDescription.setAbout("vblaldsaldlas");
//        personalDescription.setInterest("sadsadsadsadsadas");
//        List<Country> countryList = new ArrayList<>();
//        countryList.add(countryRepository.findFirstByCountry("Poland").get());
//        countryList.add(countryRepository.findFirstByCountry("Spain").get());
//        personalDescription.setVisitedCountries(countryList);
//        personalDataDTO.setPersonalDescription(personalDescription);
//        updatePersonalInformation(userRepository.findByLogin("norbert1517"),personalDataDTO);
//        System.out.println(getPersonalInformation("norbert1517"));

    }


}
