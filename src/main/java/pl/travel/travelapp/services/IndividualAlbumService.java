package pl.travel.travelapp.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.builders.IndividualAlbumBuilder;
import pl.travel.travelapp.builders.IndividualAlbumDTOBuilder;
import pl.travel.travelapp.builders.IndividualAlbumFullInformationBuilder;
import pl.travel.travelapp.builders.PersonalInformationDTOBuilder;
import pl.travel.travelapp.interfaces.CoordinateInterface;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.interfaces.SharedAlbumInterface;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;
import pl.travel.travelapp.mappers.PersonalDataAlbumsToAlbumsDTOMapperClass;
import pl.travel.travelapp.models.*;
import pl.travel.travelapp.repositories.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IndividualAlbumService implements IndividualAlbumInterface, CoordinateInterface, SharedAlbumInterface {
    private IndividualAlbumRepository individualAlbumRepository;
    private CommentsRepository commentsRepository;
    private CoordinatesRepository coordinatesRepository;
    private AlbumPhotosRepository albumPhotosRepository;
    private PersonalService personalService;
    private CountryRepository countryRepository;
    private SharedAlbumRepository sharedAlbumRepository;

    public IndividualAlbumService(IndividualAlbumRepository individualAlbumRepository , CommentsRepository commentsRepository , CoordinatesRepository coordinatesRepository , AlbumPhotosRepository albumPhotosRepository , PersonalService personalService , CountryRepository countryRepository , SharedAlbumRepository sharedAlbumRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
        this.commentsRepository = commentsRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.albumPhotosRepository = albumPhotosRepository;
        this.personalService = personalService;
        this.countryRepository = countryRepository;
        this.sharedAlbumRepository = sharedAlbumRepository;
    }

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private boolean checkIfIndividualAlbumDTOisCorrect(IndividualAlbumDTO individualAlbumDTO) {
        //check Coordinates
        Coordinates coordinates = individualAlbumDTO.getCoordinate();
        if ( coordinates == null ) return false;
        if ( coordinates.getLang() > 180 || coordinates.getLang() < -180 ) return false;
        if ( coordinates.getLat() > 90 || coordinates.getLat() < -90 ) return false;
        if ( coordinates.getPlace().isBlank() ) return false;
        if ( individualAlbumDTO.getName().isBlank() ) return false;
        if ( individualAlbumDTO.getDescription().isBlank() ) return false;
        return true;
    }

    @Override
    @Transactional
    public ResponseEntity <IndividualAlbumDTO> addNewAlbum(Principal principal , IndividualAlbumDTO individualAlbumDTO) {
        PersonalData user = personalService.getPersonalInformationWithAlbums(principal.getName());
        if ( !checkIfIndividualAlbumDTOisCorrect(individualAlbumDTO) )
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        Country country = countryRepository.findById(individualAlbumDTO.getCoordinate().getCountry().getId()).get();
        Coordinates coord = individualAlbumDTO.getCoordinate();
        coord.setCountry(country);
        Coordinates coordinates = coordinatesRepository.save(coord);
        IndividualAlbum individualAlbum = new IndividualAlbumBuilder()
                .setPublic(individualAlbumDTO.isPublic())
                .setCoordinate(coordinates)
                .setName(individualAlbumDTO.getName())
                .setDescription(individualAlbumDTO.getDescription())
                .createIndividualAlbum();
        if ( individualAlbumDTO.getSharedAlbumList() != null ) {
            individualAlbumDTO.getSharedAlbumList().forEach(sharedAlbum -> {
                Optional <PersonalData> personalData = personalService.getPersonalInformation(sharedAlbum.getUserId());
                if ( personalData.isPresent() ) {
                    SharedAlbum shared = new SharedAlbum().build(personalData.get());
                    individualAlbum.addNewUserToAlbumShare(shared);
                }
            });
        }
        user.addAlbum(individualAlbum);
        individualAlbum.setOwner(user);
        IndividualAlbum userAlbum = individualAlbumRepository.save(individualAlbum);
        return new ResponseEntity <>(new IndividualAlbumDTOBuilder()
                .setCoordinate(userAlbum.getCoordinate())
                .setDescription(userAlbum.getDescription())
                .setName(userAlbum.getName())
                .setMainPhoto(userAlbum.getMainPhoto())
                .setPersonalInformationDTO(new PersonalInformationDTOBuilder()
                        .setId(user.getId())
                        .setName(user.getFirstName())
                        .setSurName(user.getSurName())
                        .setPhoto(user.getProfilePicture())
                        .createPersonalInformationDTO())
                .setPublic(userAlbum.isPublic())
                .setId(userAlbum.getId())
                .setSharedAlbum(userAlbum.getSharedAlbum())
                .createIndividualAlbumDTO() , HttpStatus.OK);
    }


    @Override
    public ResponseEntity deleteAlbum(Principal principal , long id) {
        if ( id < 0 ) return new ResponseEntity("invalid id" , HttpStatus.BAD_REQUEST);
        if ( individualAlbumRepository.findIndividualAlbumById(id , personalService.getPersonalInformation(principal.getName()).getId()).isPresent() ) {
            individualAlbumRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("No such album was found or it is not yours" , HttpStatus.FORBIDDEN);
        }


    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <IndividualAlbum> findAlbum(Principal principal , long id) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional <IndividualAlbum> individualAlbum = individualAlbumRepository.findIndividualAlbumByIdAndReturnFullInformation(id , user.getId());
        if ( individualAlbum.isPresent() ) return new ResponseEntity(individualAlbum.get() , HttpStatus.OK);
        return new ResponseEntity("The album does not exist or you do not have permission" , HttpStatus.FORBIDDEN);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <IndividualAlbum> findIndividualAlbumByIdOnlyOwner(Principal principal , long id) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        IndividualAlbum individualAlbum = individualAlbumRepository.findIndividualAlbumByOwnerAndReturnFullInformation(id , user.getId()).get();
        return new ResponseEntity(individualAlbum , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAllUserAlbums(Principal principal) {
        PersonalData user = personalService.getPersonalInformationWithAlbums(principal.getName());
        if ( user.getAlbums().isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(user) , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <BasicIndividualAlbumDTO>> findAlbumsByUser(long id) {
        List <IndividualAlbum> individualAlbum = individualAlbumRepository.findUserPublicAlbumsByUserId(id);
        return new ResponseEntity(IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum) , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByName(String name , int page) {
        List <IndividualAlbum> individualAlbums = individualAlbumRepository.findUserAlbumsByAlbumName(name , PageRequest.of(page , 5));
        if ( individualAlbums.isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(individualAlbums) , HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByUserId(long id) {
        List <IndividualAlbum> individualAlbums = individualAlbumRepository.findUserPublicAlbumsByUserId(id);
        if ( individualAlbums.isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(individualAlbums) , HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity <List <IndividualAlbumFullInformationBuilder>> findFullAlbumsByUserId(long id , Principal principal) {
        PersonalData personalData = personalService.getPersonalInformation(principal.getName());
        List <IndividualAlbum> individualAlbums = individualAlbumRepository.findFullUserPublicAlbumsByUserId(id);
        if ( individualAlbums.isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        Set <IndividualAlbumFullInformationBuilder> albums = new HashSet <>();
        for (IndividualAlbum album : individualAlbums) {
            if ( personalData.getId() != album.getOwner().getId() ) {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            albums.add(
                    IndividualAlbumFullInformationBuilder.builder()
                            .name(album.getName())
                            .coordinate(album.getCoordinate())
                            .id(album.getId())
                            .description(album.getDescription())
                            .personalInformationDTO(new PersonalInformationDTO(album.getOwner().getId() , album.getOwner().getFirstName() , album.getOwner().getSurName() , album.getOwner().getProfilePicture()))
                            .mainPhoto(album.getMainPhoto())
                            .sharedAlbumList(album.getSharedAlbum())
                            .isPublic(album.isPublic())
                            .photos(album.getPhotos())
                            .build()
            );
        }
        return new ResponseEntity(albums , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <BasicIndividualAlbumDTO> modifyAlbum(Principal principal , BasicIndividualAlbumDTO basicIndividualAlbumDTO) {
        PersonalData personalData = personalService.getPersonalInformation(principal.getName());
        Optional <IndividualAlbum> individualAlbum = individualAlbumRepository.findIndividualAlbumByOwner(basicIndividualAlbumDTO.getId() , personalData.getId());
        if ( individualAlbum.isPresent() ) {
            IndividualAlbum album = individualAlbum.get();
            if ( basicIndividualAlbumDTO.getCoordinate() != null )
                album.setCoordinate(basicIndividualAlbumDTO.getCoordinate());
            if ( basicIndividualAlbumDTO.getDescription() != null )
                album.setDescription(basicIndividualAlbumDTO.getDescription());
            if ( basicIndividualAlbumDTO.getName() != null ) album.setName(basicIndividualAlbumDTO.getName());
            return new ResponseEntity(IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbumRepository.save(album)) , HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public IndividualAlbum findUserAlbum(Principal principal , long id) {
        return individualAlbumRepository.findIndividualAlbumByOwner(id , personalService.getPersonalInformation(principal.getName()).getId()).get();
    }

    @Transactional
    @Override
    public IndividualAlbum saveAlbum(IndividualAlbum individualAlbum) {
        return individualAlbumRepository.save(individualAlbum);
    }

    @Transactional
    public ResponseEntity <IndividualAlbumDTO> setMainPhotoToIndividualAlbum(Principal principal , MultipartFile file , long id) {
        IndividualAlbum individualAlbum = findUserAlbum(principal , id);
        if ( individualAlbum != null ) {
            try {
                String path = "user/" + principal.getName() + "/album/" + id + "/picture/main/" + file.getOriginalFilename();
                BlobId blobId = BlobId.of(bucket , path);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                storage.create(blobInfo , file.getBytes());
                individualAlbum.setMainPhoto(url + path);
                IndividualAlbum userAlbum = saveAlbum(individualAlbum);
                return new ResponseEntity <>(new IndividualAlbumDTOBuilder()
                        .setCoordinate(userAlbum.getCoordinate())
                        .setDescription(userAlbum.getDescription())
                        .setName(userAlbum.getName())
                        .setMainPhoto(userAlbum.getMainPhoto())
                        .setPersonalInformationDTO(new PersonalInformationDTOBuilder()
                                .setId(userAlbum.getOwner().getId())
                                .setName(userAlbum.getOwner().getFirstName())
                                .setSurName(userAlbum.getOwner().getSurName())
                                .setPhoto(userAlbum.getOwner().getProfilePicture())
                                .createPersonalInformationDTO())
                        .setPublic(userAlbum.isPublic())
                        .setId(userAlbum.getId())
                        .setSharedAlbum(userAlbum.getSharedAlbum())
                        .createIndividualAlbumDTO() , HttpStatus.OK);
            } catch ( IOException e ) {
                e.printStackTrace();
                return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity <>(HttpStatus.FORBIDDEN);
    }

    @Transactional
    public ResponseEntity <List <BasicIndividualAlbumDTO>> getAvailableAlbums(Principal principal) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        List <SharedAlbum> sharedAlbum = sharedAlbumRepository.findAvailableAlbums(user.getId());
        return new ResponseEntity(sharedAlbum.stream().map(shared -> new BasicIndividualAlbumDTO(shared.getIndividualAlbum())).sorted().collect(Collectors.toList()) , HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity deleteShared(Principal principal , List <Long> sharedIds) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        sharedIds.forEach(id -> {
                    Optional <SharedAlbum> sharedAlbums = sharedAlbumRepository.findById(id);
                    if ( sharedAlbums.isPresent() ) {
                        IndividualAlbum album = sharedAlbums.get().getIndividualAlbum();
                        if ( album.getOwner().getId() == user.getId() ) {
                            album.deleteUserFromAlbumShare(sharedAlbums.get());
                            individualAlbumRepository.save(album);
                        }
                    }
                }
        );
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity addShared(Principal principal , List <Long> sharedIds , Long albumId) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        List <PersonalData> users = personalService.findAllById(sharedIds);
        IndividualAlbum album = individualAlbumRepository.findById(albumId).get();
        if ( user.getId() != album.getOwner().getId() ) return new ResponseEntity(HttpStatus.FORBIDDEN);
        users.forEach(person -> {
                    SharedAlbum sharedAlbum = new SharedAlbum().build(person);
                    album.addNewUserToAlbumShare(sharedAlbum);
                    individualAlbumRepository.save(album);
                }
        );
        return new ResponseEntity(HttpStatus.CREATED);
    }

}