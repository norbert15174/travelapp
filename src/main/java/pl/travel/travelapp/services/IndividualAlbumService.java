package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.builders.IndividualAlbumBuilder;
import pl.travel.travelapp.builders.IndividualAlbumDTOBuilder;
import pl.travel.travelapp.builders.PersonalInformationDTOBuilder;
import pl.travel.travelapp.interfaces.CoordinateInterface;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.interfaces.SharedAlbumInterface;
import pl.travel.travelapp.models.Coordinates;
import pl.travel.travelapp.models.Country;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.*;

import java.security.Principal;
import java.util.List;

@Service
public class IndividualAlbumService implements IndividualAlbumInterface, CoordinateInterface, SharedAlbumInterface {
    private IndividualAlbumRepository individualAlbumRepository;
    private CommentsRepository commentsRepository;
    private CoordinatesRepository coordinatesRepository;
    private AlbumPhotosRepository albumPhotosRepository;
    private PersonalService personalService;

    public IndividualAlbumService(IndividualAlbumRepository individualAlbumRepository , CommentsRepository commentsRepository , CoordinatesRepository coordinatesRepository , AlbumPhotosRepository albumPhotosRepository , PersonalService personalService) {
        this.individualAlbumRepository = individualAlbumRepository;
        this.commentsRepository = commentsRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.albumPhotosRepository = albumPhotosRepository;
        this.personalService = personalService;
    }

    private boolean checkIfIndividualAlbumDTOisCorrect(IndividualAlbumDTO individualAlbumDTO){
        //check Coordinates
        Coordinates coordinates = individualAlbumDTO.getCoordinate();
        if(coordinates == null) return false;
        if(coordinates.getLang() > 180 || coordinates.getLang() < -180 ) return false;
        if(coordinates.getLat() > 90 || coordinates.getLat() < -90 ) return false;
        if(coordinates.getPlace().isBlank()) return false;
        if(individualAlbumDTO.getName().isBlank()) return false;
        if(individualAlbumDTO.getDescription().isBlank()) return false;
        return true;
    }

    @Override
    @Transactional
    public ResponseEntity <IndividualAlbumDTO> addNewAlbum(Principal principal , IndividualAlbumDTO individualAlbumDTO) {
        PersonalData user = personalService.getPersonalInformationWithAlbums(principal.getName());
        if(!checkIfIndividualAlbumDTOisCorrect(individualAlbumDTO)) return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        IndividualAlbum individualAlbum = new IndividualAlbumBuilder()
                .setPublic(individualAlbumDTO.isPublic())
                .setCoordinate(individualAlbumDTO.getCoordinate())
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
                .createIndividualAlbumDTO(), HttpStatus.OK);
    }


    public void test(){
        IndividualAlbumDTO individualAlbumDTO = new IndividualAlbumDTO();
        Coordinates coordinates = new Coordinates();
        coordinates.setLang(25.53);
        coordinates.setLat(25.53);
        coordinates.setPlace("Poland");
        individualAlbumDTO.setCoordinate(coordinates);
        individualAlbumDTO.setDescription("asd");
        individualAlbumDTO.setName("albuym");
        individualAlbumDTO.setPublic(true);

    }

    @Override
    public ResponseEntity deleteAlbum(Principal principal , long id) {
        return null;
    }

    @Override
    public ResponseEntity <IndividualAlbumDTO> findAlbum(Principal principal , long id) {
        return null;
    }

    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAllUserAlbums(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByUser(long id) {
        return null;
    }

    @Override
    public ResponseEntity <IndividualAlbumDTO> findAlbumByName(String name) {
        return null;
    }

    @Override
    public ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByUserName(long id) {
        return null;
    }

    @Override
    public ResponseEntity <IndividualAlbumDTO> modifyAlbum(Principal principal , long id) {
        return null;
    }
}
