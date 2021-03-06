package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.albums.AlbumDTO;
import pl.travel.travelapp.builders.IndividualAlbumFullInformationBuilder;
import pl.travel.travelapp.entites.IndividualAlbum;

import java.security.Principal;
import java.util.List;

public interface IndividualAlbumInterface {
    ResponseEntity <IndividualAlbumDTO> addNewAlbum(Principal principal , IndividualAlbumDTO individualAlbumDTO);

    ResponseEntity deleteAlbum(Principal principal , long id);

    ResponseEntity <IndividualAlbum> findAlbum(Principal principal , long id);

    ResponseEntity <IndividualAlbum> findIndividualAlbumByIdOnlyOwner(Principal principal , long id);

    ResponseEntity <List <IndividualAlbumDTO>> findAllUserAlbums(Principal principal);

    ResponseEntity <List <BasicIndividualAlbumDTO>> findAlbumsByUser(long id);

    ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByName(String name , int page);

    ResponseEntity <List <IndividualAlbumDTO>> findAlbumsByUserId(long id);

    ResponseEntity <List <IndividualAlbumFullInformationBuilder>> findFullAlbumsByUserId(long id , Principal principal);

    ResponseEntity <BasicIndividualAlbumDTO> modifyAlbum(Principal principal , BasicIndividualAlbumDTO basicIndividualAlbumDTO, Long id);

    IndividualAlbum findUserAlbum(Principal principal , long id);

    IndividualAlbum saveAlbum(IndividualAlbum individualAlbum);

    @Transactional
    ResponseEntity <IndividualAlbumDTO> setMainPhotoToIndividualAlbum(Principal principal , MultipartFile file , long id);

    @Transactional
    ResponseEntity <List <AlbumDTO>> getAvailableAlbums(Principal principal);

    @Transactional
    ResponseEntity <AlbumDTO> getAlbumFullInformation(Principal principal , Long albumId);

    @Transactional
    ResponseEntity deleteShared(Principal principal , List <Long> sharedIds);

    @Transactional
    ResponseEntity addShared(Principal principal , List <Long> sharedIds , Long albumId);
}
