package pl.travel.travelapp.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.builders.PersonalInformationDTOBuilder;
import pl.travel.travelapp.models.FriendsRequest;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;

import java.util.ArrayList;
import java.util.List;

public class PersonalDataAlbumsToAlbumsDTOMapperClass {
    private static ModelMapper individualAlbumToIndividualAlbumDTO() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap <IndividualAlbum, IndividualAlbumDTO>() {
            @Override
            protected void configure() {
                map().setPublic(source.isPublic());
                map().setName(source.getName());
                map().setDescription(source.getDescription());
                map().setCoordinate(source.getCoordinate());
                map().setId(source.getId());
                map().setMainPhoto(source.getMainPhoto());
            }
        });
        return modelMapper;
    }



    //Return mapped model
    public static List <IndividualAlbumDTO> mapPersonalDataToAlbumsDTO(PersonalData user) {
        List<IndividualAlbumDTO> userAlbums = new ArrayList <>();
        user.getAlbums().forEach(album ->
            {
                IndividualAlbumDTO userAlbum = individualAlbumToIndividualAlbumDTO().map(album,IndividualAlbumDTO.class);
                userAlbum.setPersonalInformationDTO(new PersonalInformationDTOBuilder()
                        .setPhoto(user.getProfilePicture())
                        .setId(user.getId())
                        .setName(user.getFirstName())
                        .setSurName(user.getSurName())
                        .createPersonalInformationDTO());
                userAlbums.add(userAlbum);
            }
        );
        return userAlbums;
    }
    //Return mapped model
    public static List <IndividualAlbumDTO> mapPersonalDataToAlbumsDTO(List<IndividualAlbum> albums) {
        List<IndividualAlbumDTO> userAlbums = new ArrayList <>();
        albums.forEach(album ->
                {
                    IndividualAlbumDTO userAlbum = individualAlbumToIndividualAlbumDTO().map(album,IndividualAlbumDTO.class);
                    userAlbum.setPersonalInformationDTO(new PersonalInformationDTOBuilder()
                            .setPhoto(album.getOwner().getProfilePicture())
                            .setId(album.getOwner().getId())
                            .setName(album.getOwner().getFirstName())
                            .setSurName(album.getOwner().getSurName())
                            .createPersonalInformationDTO());
                    userAlbums.add(userAlbum);
                }
        );
        return userAlbums;
    }
}
