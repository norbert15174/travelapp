package pl.travel.travelapp.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.builders.IndividualAlbumBuilder;
import pl.travel.travelapp.builders.IndividualAlbumDTOBuilder;
import pl.travel.travelapp.builders.PersonalInformationDTOBuilder;
import pl.travel.travelapp.interfaces.CoordinateInterface;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.interfaces.SharedAlbumInterface;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;
import pl.travel.travelapp.mappers.PersonalDataAlbumsToAlbumsDTOMapperClass;
import pl.travel.travelapp.models.Coordinates;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class IndividualAlbumService implements IndividualAlbumInterface, CoordinateInterface, SharedAlbumInterface {
    private IndividualAlbumRepository individualAlbumRepository;
    private CommentsRepository commentsRepository;
    private CoordinatesRepository coordinatesRepository;
    private AlbumPhotosRepository albumPhotosRepository;
    private PersonalService personalService;
    private CountryRepository countryRepository;

    public IndividualAlbumService(IndividualAlbumRepository individualAlbumRepository , CommentsRepository commentsRepository , CoordinatesRepository coordinatesRepository , AlbumPhotosRepository albumPhotosRepository , PersonalService personalService , CountryRepository countryRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
        this.commentsRepository = commentsRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.albumPhotosRepository = albumPhotosRepository;
        this.personalService = personalService;
        this.countryRepository = countryRepository;
    }

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
        Coordinates coordinates = coordinatesRepository.save(individualAlbumDTO.getCoordinate());
        IndividualAlbum individualAlbum = new IndividualAlbumBuilder()
                .setPublic(individualAlbumDTO.isPublic())
                .setCoordinate(coordinates)
                .setName(individualAlbumDTO.getName())
                .setDescription(individualAlbumDTO.getDescription())
                .createIndividualAlbum();
        user.addAlbum(individualAlbum);
        individualAlbum.setOwner(user);
        IndividualAlbum userAlbum = individualAlbumRepository.save(individualAlbum);
        return new ResponseEntity <>(new IndividualAlbumDTOBuilder()
                .setCoordinate(userAlbum.getCoordinate())
                .setDescription(userAlbum.getDescription())
                .setName(userAlbum.getName())
                .setPersonalInformationDTO(new PersonalInformationDTOBuilder()
                        .setId(user.getId())
                        .setName(user.getFirstName())
                        .setSurName(user.getSurName())
                        .setPhoto(user.getProfilePicture())
                        .createPersonalInformationDTO())
                .setPublic(userAlbum.isPublic())
                .setId(userAlbum.getId())
                .createIndividualAlbumDTO() , HttpStatus.OK);
    }


    @Override
    public ResponseEntity deleteAlbum(Principal principal , long id) {
        if(id < 0) return new ResponseEntity("invalid id",HttpStatus.BAD_REQUEST);
        if(individualAlbumRepository.findIndividualAlbumById(id,personalService.getPersonalInformation(principal.getName()).getId()).isPresent()){
            individualAlbumRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity("No such album was found or it is not yours",HttpStatus.FORBIDDEN);
        }


    }

    @Override
    public ResponseEntity <IndividualAlbum> findAlbum(Principal principal , long id) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        Optional<IndividualAlbum> individualAlbum = individualAlbumRepository.findIndividualAlbumByIdAndReturnFullInformation(id,user.getId());
        if(individualAlbum.isPresent()) return new ResponseEntity (individualAlbum, HttpStatus.OK);
        return new ResponseEntity("The album does not exist or you do not have permission",HttpStatus.FORBIDDEN);
    }


    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAllUserAlbums(Principal principal) {
        PersonalData user = personalService.getPersonalInformationWithAlbums(principal.getName());
        if ( user.getAlbums().isEmpty() ) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity <>(PersonalDataAlbumsToAlbumsDTOMapperClass.mapPersonalDataToAlbumsDTO(user) , HttpStatus.OK);
    }

    @Override
    public ResponseEntity <List <BasicIndividualAlbumDTO>> findAlbumsByUser(long id) {
        List<IndividualAlbum> individualAlbum = individualAlbumRepository.findUserPublicAlbumsByUserId(id);
        return new ResponseEntity (IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum), HttpStatus.OK);
    }


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
    public ResponseEntity <BasicIndividualAlbumDTO> modifyAlbum(Principal principal , BasicIndividualAlbumDTO basicIndividualAlbumDTO) {
        PersonalData personalData = personalService.getPersonalInformation(principal.getName());
        Optional <IndividualAlbum> individualAlbum = individualAlbumRepository.findIndividualAlbumByOwner(basicIndividualAlbumDTO.getId(),personalData.getId());
        if(individualAlbum.isPresent()){
            IndividualAlbum album = individualAlbum.get();
            if(basicIndividualAlbumDTO.getCoordinate() != null) album.setCoordinate(basicIndividualAlbumDTO.getCoordinate());
            if(basicIndividualAlbumDTO.getDescription() != null) album.setDescription(basicIndividualAlbumDTO.getDescription());
            if(basicIndividualAlbumDTO.getName() != null) album.setName(basicIndividualAlbumDTO.getName());
            return new ResponseEntity (IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbumRepository.save(album)),HttpStatus.OK);
        }
        return new ResponseEntity (HttpStatus.OK);
    }

    @Override
    public IndividualAlbum findUserAlbum(Principal principal , long id) {
        return individualAlbumRepository.findIndividualAlbumByOwner(id, personalService.getPersonalInformation(principal.getName()).getId()).get();
    }

    @Transactional
    @Override
    public IndividualAlbum saveAlbum(IndividualAlbum individualAlbum){
        return individualAlbumRepository.save(individualAlbum);
    }

}
