package pl.travel.travelapp.DTO.albums;

import lombok.*;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.DTO.PhotosDTO;
import pl.travel.travelapp.DTO.SharedUserAlbumDTO;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.SharedAlbum;
import pl.travel.travelapp.mappers.IndividualAlbumToBasicIndividualAlbumDTOMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    BasicIndividualAlbumDTO album;
    PersonalInformationDTO owner;
    Set <SharedUserAlbumDTO> shared;
    Set <PhotosDTO> photosDTOS;

    public AlbumDTO(IndividualAlbum individualAlbum) {
        setAlbumDtoParameters(individualAlbum);
    }

    public AlbumDTO build(IndividualAlbum individualAlbum) {
        setAlbumDtoParameters(individualAlbum);
        return this;
    }

    public AlbumDTO buildWithOutPhotosAndShared(IndividualAlbum individualAlbum) {
        this.album = buildBasicInvividualAlbumDTO(individualAlbum);
        this.owner = buildPersonalInformationDTO(individualAlbum);
        return this;
    }

    private void setAlbumDtoParameters(IndividualAlbum individualAlbum) {
        this.album = buildBasicInvividualAlbumDTO(individualAlbum);
        this.owner = buildPersonalInformationDTO(individualAlbum);
        this.shared = buildSharedAlbum(individualAlbum.getSharedAlbum());
        this.photosDTOS = buildPhotos(individualAlbum.getPhotos());
    }

    private BasicIndividualAlbumDTO buildBasicInvividualAlbumDTO(IndividualAlbum individualAlbum) {
        return IndividualAlbumToBasicIndividualAlbumDTOMapper.mapindividualAlbumToBasicIndividualAlbumDTO(individualAlbum);
    }

    private PersonalInformationDTO buildPersonalInformationDTO(IndividualAlbum individualAlbum) {
        PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO();
        PersonalData albumOwner = individualAlbum.getOwner();
        personalInformationDTO.setId(albumOwner.getId());
        personalInformationDTO.setName(albumOwner.getFirstName());
        personalInformationDTO.setSurName(albumOwner.getSurName());
        personalInformationDTO.setPhoto(albumOwner.getProfilePicture());
        return personalInformationDTO;
    }

    private Set <SharedUserAlbumDTO> buildSharedAlbum(List <SharedAlbum> userToShare) {
        Comparator <SharedUserAlbumDTO> comp = (SharedUserAlbumDTO o1 , SharedUserAlbumDTO o2) -> (Long.compare(o1.getId() , o2.getId()));
        Set <SharedUserAlbumDTO> sharedUsers = new TreeSet <>(comp);
        userToShare.forEach(person -> {
            SharedUserAlbumDTO shared = new SharedUserAlbumDTO(
                    person.getId() ,
                    person.getName() ,
                    person.getSurName() ,
                    person.getPhoto() ,
                    person.getUserId());
            sharedUsers.add(shared);
        });

        return sharedUsers;
    }

    private Set <PhotosDTO> buildPhotos(List <AlbumPhotos> albumPhotos) {
        Comparator <PhotosDTO> comp = (PhotosDTO o1 , PhotosDTO o2) -> (Long.compare(o1.getPhotoId() , o2.getPhotoId()));
        Set <PhotosDTO> photos = new TreeSet <>(comp);
        albumPhotos.forEach(photoAlbum -> {
            PhotosDTO photo = new PhotosDTO(
                    photoAlbum.getPhotoId() ,
                    photoAlbum.getPhotoUrl() ,
                    photoAlbum.getDescription() ,
                    photoAlbum.getComments() ,
                    photoAlbum.getTaggedList());
            photos.add(photo);
        });
        return photos;
    }


}
