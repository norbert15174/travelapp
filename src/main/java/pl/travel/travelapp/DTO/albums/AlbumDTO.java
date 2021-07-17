package pl.travel.travelapp.DTO.albums;

import lombok.*;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.DTO.PhotosDTO;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;
import pl.travel.travelapp.models.AlbumPhotos;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.SharedAlbum;

import java.util.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    BasicIndividualAlbumDTO album;
    PersonalInformationDTO owner;
    Set <PersonalInformationDTO> shared;
    Set <PhotosDTO> photosDTOS;


    public AlbumDTO build(IndividualAlbum individualAlbum){
        this.album = buildBasicInvividualAlbumDTO(individualAlbum);
        this.owner = buildPersonalInformationDTO(individualAlbum);
        this.shared = buildSharedAlbum(individualAlbum.getSharedAlbum());
        this.photosDTOS = buildPhotos(individualAlbum.getPhotos());
        return this;
    }

    private BasicIndividualAlbumDTO buildBasicInvividualAlbumDTO(IndividualAlbum individualAlbum){
        return IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum);
    }

    private PersonalInformationDTO buildPersonalInformationDTO(IndividualAlbum individualAlbum){
        PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO();
        PersonalData albumOwner = individualAlbum.getOwner();
        personalInformationDTO.setId(albumOwner.getId());
        personalInformationDTO.setName(albumOwner.getFirstName());
        personalInformationDTO.setSurName(albumOwner.getSurName());
        personalInformationDTO.setPhoto(albumOwner.getProfilePicture());
        return personalInformationDTO;
    }

    private Set<PersonalInformationDTO> buildSharedAlbum(List<SharedAlbum> shared){
        Comparator<PersonalInformationDTO> comp = (PersonalInformationDTO o1, PersonalInformationDTO o2) -> (Long.compare(o1.getId(),o2.getId()));
        Set <PersonalInformationDTO> personalInformationDTOS = new TreeSet <>(comp);
        shared.forEach(person -> {
            PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO(
                    person.getId(),
                    person.getName(),
                    person.getSurName(),
                    person.getPhoto());
            personalInformationDTOS.add(personalInformationDTO);
        });

        return personalInformationDTOS;
    }

    private Set<PhotosDTO> buildPhotos(List<AlbumPhotos> albumPhotos){
        Comparator<PhotosDTO> comp = (PhotosDTO o1, PhotosDTO o2) -> (Long.compare(o1.getPhotoId(),o2.getPhotoId()));
        Set<PhotosDTO> photos = new TreeSet <>(comp);
        albumPhotos.forEach(photoAlbum -> {
            PhotosDTO photo = new PhotosDTO(
                    photoAlbum.getPhotoId(),
                    photoAlbum.getPhotoUrl(),
                    photoAlbum.getDescription(),
                    photoAlbum.getComments(),
                    photoAlbum.getTaggedList());
            photos.add(photo);
        });
        return photos;
    }


}
