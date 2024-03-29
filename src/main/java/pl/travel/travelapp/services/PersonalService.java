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
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.DTO.PersonalDataDtoWithIndividualAlbumsDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.exceptions.CountryNotFoundException;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;
import pl.travel.travelapp.mappers.PersonalDataObjectMapperClass;
import pl.travel.travelapp.repositories.*;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.specification.criteria.UserSearchCriteria;
import pl.travel.travelapp.validators.user.CountryValidator;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonalService {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private final PersonalDataRepository personalDataRepository;
    private final PersonalDescriptionRepository personalDescriptionRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final IndividualAlbumRepository individualAlbumRepository;
    private final IPersonalQueryService personalQueryService;
    private final SharedAlbumRepository sharedAlbumRepository;
    private final TaggedPhotoRepository taggedPhotoRepository;
    private final CommentsRepository commentsRepository;
    private final CountryValidator countryValidator;

    @Autowired
    public PersonalService(PersonalDataRepository personalDataRepository , PersonalDescriptionRepository personalDescriptionRepository , CountryRepository countryRepository , UserRepository userRepository , IndividualAlbumRepository individualAlbumRepository , IPersonalQueryService personalQueryService , SharedAlbumRepository sharedAlbumRepository , TaggedPhotoRepository taggedPhotoRepository , CommentsRepository commentsRepository , CountryValidator countryValidator) {
        this.personalDataRepository = personalDataRepository;
        this.personalDescriptionRepository = personalDescriptionRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.individualAlbumRepository = individualAlbumRepository;
        this.personalQueryService = personalQueryService;
        this.sharedAlbumRepository = sharedAlbumRepository;
        this.taggedPhotoRepository = taggedPhotoRepository;
        this.commentsRepository = commentsRepository;
        this.countryValidator = countryValidator;
    }

    public ResponseEntity <PersonalDataDTO> getUserProfile(Principal user) {
        PersonalData userData = personalQueryService.getPersonalInformation(user.getName());
        List <IndividualAlbum> individualAlbum = individualAlbumRepository.findUserAlbumsByUserId(userData.getId());
        return new ResponseEntity(new PersonalDataDtoWithIndividualAlbumsDTO(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData) , IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum)) , HttpStatus.OK);
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
            PersonalData userProfile = fillPersonalInformation(personalQueryService.getPersonalInformation(user.getName()) , userUpdate);
            personalDataRepository.save(userProfile);
            setCorrectDataInTaggedAndSharedUser(userProfile);
            return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userProfile) , HttpStatus.OK);
        } catch ( Exception e ) {
            System.err.println(e.getMessage());
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
    }

    private PersonalData fillPersonalInformation(PersonalData userProfile , PersonalDataDTO userUpdate) {
        if ( userUpdate.getNationality() != null )
            userProfile.setNationality(countryRepository.findById(userUpdate.getNationality().getId()).get());
        if ( userUpdate.getPhoneNumber() != 0 ) userProfile.setPhoneNumber(userUpdate.getPhoneNumber());
        if ( userUpdate.getBirthday() != null ) userProfile.setBirthDate(userUpdate.getBirthday());
        if ( userUpdate.getSurName() != null ) userProfile.setSurName(userUpdate.getSurName());
        if ( userUpdate.getFirstName() != null ) userProfile.setFirstName(userUpdate.getFirstName());
        if ( userUpdate.getProfilePicture() != null ) userProfile.setProfilePicture(userUpdate.getProfilePicture());
        if ( userUpdate.getBackgroundPicture() != null )
            userProfile.setBackgroundPicture(userUpdate.getBackgroundPicture());
        PersonalDescription personalDescription = userProfile.getPersonalDescription();
        if ( userUpdate.getPersonalDescription() != null ) {
            if ( userUpdate.getPersonalDescription().getVisitedCountries() != null ) {
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
        return personalQueryService.getPersonalInformation(username);
    }

    @Transactional
    public Optional <PersonalData> getPersonalInformation(Long id) {
        return personalQueryService.findById(id);
    }

    @Transactional(readOnly = true)
    public PersonalData getPersonalInformationWithAlbums(String username) {
        return personalDataRepository.findPersonalDataWithAlbumsByUserId(getPersonalInformation(username).getId());
    }

    @Transactional
    public ResponseEntity <PersonalDataDTO> setPersonalDataProfilePicture(MultipartFile file , Principal user) {
        try {
            PersonalData userData = personalQueryService.getPersonalInformation(user.getName());
            String path = "user/" + user.getName() + "/picture/" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucket , path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo , file.getBytes());
            if ( userData.getProfilePicture() != null ) {
                storage.delete(BlobId.of("telephoners" , userData.getProfilePicture().split(bucket + "/")[1]));
            }
            userData.setProfilePicture(url + path);
            PersonalData savedUser = personalDataRepository.save(userData);
            setCorrectDataInTaggedAndSharedUser(savedUser);
            return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(personalDataRepository.findPersonalDataByUserId(userData.getId())) , HttpStatus.OK);
        } catch ( IOException e ) {
            e.printStackTrace();
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }
    }

    @Transactional
    public ResponseEntity <PersonalDataDTO> setPersonalDataBackgroundPicture(MultipartFile file , Principal user) {
        PersonalData userData = personalQueryService.getPersonalInformation(user.getName());
        String path = "user/" + user.getName() + "/background/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket , path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo , file.getBytes());
            if ( userData.getBackgroundPicture() != null ) {
                storage.delete(BlobId.of("telephoners" , userData.getBackgroundPicture().split(bucket + "/")[1]));
            }
            userData.setBackgroundPicture(url + path);
            personalDataRepository.save(userData);
            return new ResponseEntity <>(PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(personalDataRepository.findPersonalDataByUserId(userData.getId())) , HttpStatus.OK);
        } catch ( IOException e ) {
            System.err.println(e.getMessage());
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    public ResponseEntity <List <Country>> getCountries() {
        return new ResponseEntity <>(countryRepository.findAll() , HttpStatus.OK);
    }

    @Transactional
    public List <PersonalData> findAllById(List <Long> ids) {
        return personalDataRepository.findAllById(ids);
    }

    @Transactional
    public ResponseEntity <PersonalDataDtoWithIndividualAlbumsDTO> getUserProfileInformationWithAlbums(Principal principal , long id) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        Optional <PersonalData> userData = personalDataRepository.findPersonalDataByUserIdWithSharedAndOwnedAlbums(id , user.getId());
        if ( userData.isPresent() ) {
            PersonalDataDTO personalDataDTO = PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData.get());
            List <IndividualAlbum> albums = userData.get().getAlbums().stream().filter(album -> album.isPublic() || album.getOwner().getId() == user.getId() || album.getSharedAlbum().stream().anyMatch(al -> al.getUserId() == user.getId())).collect(Collectors.toList());
            List <BasicIndividualAlbumDTO> individualAlbumDTOS = IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(albums);
            return new ResponseEntity <>(new PersonalDataDtoWithIndividualAlbumsDTO(personalDataDTO , individualAlbumDTOS) , HttpStatus.OK);
        } else {
            userData = personalDataRepository.findPersonalDataOptionalById(id);
            if ( userData.isPresent() ) {
                PersonalDataDTO personalDataDTO = PersonalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(userData.get());
                return new ResponseEntity <>(new PersonalDataDtoWithIndividualAlbumsDTO(personalDataDTO , new ArrayList <>()) , HttpStatus.OK);
            }
        }
        return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity <PersonalInformationDTO> getBasicUserInformation(Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        return new ResponseEntity(new PersonalInformationDTO(user) , HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity <List <PersonalInformationDTO>> getUsersBySearchCriteria(Principal principal , UserSearchCriteria criteria , Integer page) {
        try {
            if ( criteria.isFromExist() ) {
                countryValidator.validate(Collections.singleton(criteria.getFrom()));
            }
        } catch ( CountryNotFoundException ex ) {
            return new ResponseEntity(ex.getMessage() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(personalQueryService.getUsersBySearchCriteria(criteria , page) , HttpStatus.OK);
    }

    private void setCorrectDataInTaggedAndSharedUser(PersonalData userProfile) {
        Set <TaggedUser> taggedUsers = taggedPhotoRepository.findUserTagByUserId(userProfile.getId());
        List <SharedAlbum> sharedUsers = sharedAlbumRepository.findAvailableAlbums(userProfile.getId());
        Set <Comments> comments = commentsRepository.findCommentsByUserId(userProfile.getId());
        taggedUsers.forEach(tag -> setAndUpdateTaggedUser(tag , userProfile));
        sharedUsers.forEach(shared -> setAndUpdateSharedUser(shared , userProfile));
        comments.forEach(comment -> setAndUpdateComments(comment , userProfile));
    }

    private void setAndUpdateComments(Comments comments , PersonalData personalData) {
        comments.setName(personalData.getFirstName());
        comments.setSurName(personalData.getSurName());
        comments.setPhoto(personalData.getProfilePicture());
        commentsRepository.save(comments);
    }

    private void setAndUpdateTaggedUser(TaggedUser taggedUser , PersonalData personalData) {
        taggedUser.setName(personalData.getFirstName());
        taggedUser.setSurName(personalData.getSurName());
        taggedUser.setPhoto(personalData.getProfilePicture());
        taggedPhotoRepository.save(taggedUser);
    }

    private void setAndUpdateSharedUser(SharedAlbum sharedAlbum , PersonalData personalData) {
        sharedAlbum.setName(personalData.getFirstName());
        sharedAlbum.setSurName(personalData.getSurName());
        sharedAlbum.setPhoto(personalData.getProfilePicture());
        sharedAlbumRepository.save(sharedAlbum);
    }

}
