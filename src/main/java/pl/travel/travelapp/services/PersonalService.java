package pl.travel.travelapp.services;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.DTO.PersonalDataDtoWithIndividualAlbumsDTO;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;
import pl.travel.travelapp.mappers.PersonalDataObjectMapperClass;
import pl.travel.travelapp.models.Country;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.PersonalDescription;
import pl.travel.travelapp.repositories.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    private IndividualAlbumRepository individualAlbumRepository;

    @Autowired
    public PersonalService(PersonalDataRepository personalDataRepository , PersonalDescriptionRepository personalDescriptionRepository , CountryRepository countryRepository , UserRepository userRepository , IndividualAlbumRepository individualAlbumRepository) {
        this.personalDataRepository = personalDataRepository;
        this.personalDescriptionRepository = personalDescriptionRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.individualAlbumRepository = individualAlbumRepository;
    }

    public ResponseEntity <PersonalDataDTO> getUserProfile(Principal user) {
        try {
            PersonalData userData = userRepository.findPersonalDataByUser(user.getName()).getPersonalData();
            List <IndividualAlbum> individualAlbum = individualAlbumRepository.findUserAlbumsByUserId(userData.getId());
            return new ResponseEntity(new PersonalDataDtoWithIndividualAlbumsDTO(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData) , IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum)) , HttpStatus.OK);
        } catch ( NullPointerException e ) {
            System.err.println("User doesn't exist");
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity <PersonalDataDTO> getUserProfileInformation(long id) {
        try {
            PersonalData userData = personalDataRepository.findPersonalDataByUserId(id);
            return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData) , HttpStatus.OK);
        } catch ( NullPointerException e ) {
            System.err.println("User doesn't exist");
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity <PersonalDataDTO> updatePersonalInformation(Principal user , PersonalDataDTO userUpdate) {
        try {
            PersonalData userProfile = userRepository.findPersonalDataByUser(user.getName()).getPersonalData();
            userProfile = fillPersonalInformation(getPersonalInformation(user.getName()) , userUpdate);
            try {
                personalDataRepository.save(userProfile);
                return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userProfile) , HttpStatus.OK);
            } catch ( Exception e ) {
                System.err.println(e.getMessage());
                return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
            }
        } catch ( NullPointerException e ) {
            System.err.println("User doesn't exist");
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }

    }

    private PersonalData fillPersonalInformation(PersonalData userProfile , PersonalDataDTO userUpdate) {
        if ( userUpdate.getNationality() != null ) userProfile.setNationality(countryRepository.findById(userUpdate.getNationality().getId()).get());
        if ( userUpdate.getPhoneNumber() != 0 ) userProfile.setPhoneNumber(userUpdate.getPhoneNumber());
        if ( userUpdate.getBirthday() != null ) userProfile.setBirthDate(userUpdate.getBirthday());
        if ( userUpdate.getSurName() != null ) userProfile.setSurName(userUpdate.getSurName());
        if ( userUpdate.getFirstName() != null ) userProfile.setFirstName(userUpdate.getFirstName());
        if ( userUpdate.getProfilePicture() != null ) userProfile.setProfilePicture(userUpdate.getProfilePicture());
        if ( userUpdate.getBackgroundPicture() != null ) userProfile.setBackgroundPicture(userUpdate.getBackgroundPicture());
        PersonalDescription personalDescription = userProfile.getPersonalDescription();
        if ( userUpdate.getPersonalDescription() != null ) {
            if ( userUpdate.getPersonalDescription().getVisitedCountries() != null ){
                personalDescription.setVisitedCountries(userUpdate.getPersonalDescription().getVisitedCountries());
            }
            if ( userUpdate.getPersonalDescription().getInterest() != null )
                personalDescription.setInterest(userUpdate.getPersonalDescription().getInterest());
            if ( userUpdate.getPersonalDescription().getAbout() != null )
                personalDescription.setAbout(userUpdate.getPersonalDescription().getAbout());
            userProfile.setPersonalDescription(personalDescription);
        }
        return userProfile;
    }


    //return personal information (PersonalData and PersonalDescription as field)
    @Transactional(readOnly = true)
    public PersonalData getPersonalInformation(String username) {
        return userRepository.findPersonalDataByUser(username).getPersonalData();
    }

    @Transactional
    public Optional <PersonalData> getPersonalInformation(Long id) {
        return personalDataRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public PersonalData getPersonalInformationWithAlbums(String username) {
        return personalDataRepository.findPersonalDataWithAlbumsByUserId(getPersonalInformation(username).getId());
    }

    @Transactional
    public ResponseEntity <PersonalDataDTO> setPersonalDataProfilePicture(MultipartFile file , Principal user) {
        try {
            PersonalData userData = userRepository.findPersonalDataByUser(user.getName()).getPersonalData();
            String path = "user/" + user.getName() + "/picture/" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucket , path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo , file.getBytes());
            if ( userData.getProfilePicture() != null ) {
                storage.delete(BlobId.of("telephoners" , userData.getProfilePicture().split(bucket + "/")[1]));
            }
            userData.setProfilePicture(url + path);
            personalDataRepository.save(userData);
            return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(personalDataRepository.findPersonalDataByUserId(userData.getId())) , HttpStatus.OK);
        } catch ( IOException e ) {
            e.printStackTrace();
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }
    }

    @Transactional
    public ResponseEntity <PersonalDataDTO> setPersonalDataBackgroundPicture(MultipartFile file , Principal user) {
        PersonalData userData = userRepository.findPersonalDataByUser(user.getName()).getPersonalData();
        String path = "user/" + user.getName() + "/background/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket , path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo , file.getBytes());
            if ( userData.getBackgroundPicture() != null ) {
                storage.delete(BlobId.of("telephoners" , userData.getBackgroundPicture().split(bucket + "/")[1]));
            }
            userData.setProfilePicture(url + path);
            personalDataRepository.save(userData);
            return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(personalDataRepository.findPersonalDataByUserId(userData.getId())) , HttpStatus.OK);
        } catch ( IOException e ) {
            System.err.println(e.getMessage());
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    public ResponseEntity <List<Country>> getCountries(){
        return new ResponseEntity <>(countryRepository.findAll(),HttpStatus.OK);
    }


}
