package pl.travel.travelapp.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import pl.travel.travelapp.DTO.BasicIndividualAlbumDTO;
import pl.travel.travelapp.entites.IndividualAlbum;

import java.util.ArrayList;
import java.util.List;

public class IndividualAlbumToBasicIndividualAlbumDTOMapper {


    private static ModelMapper individualAlbumToBasicIndividualAlbumDTO() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap <IndividualAlbum, BasicIndividualAlbumDTO>() {
            @Override
            protected void configure() {
                map().setCoordinate(source.getCoordinate());
                map().setDescription(source.getDescription());
                map().setId(source.getId());
                map().setMainPhoto(source.getMainPhoto());
                map().setName(source.getName());
                map().setPublic(source.isPublic());
            }
        });
        return modelMapper;
    }


    public static List <BasicIndividualAlbumDTO> mapindividualAlbumToBasicIndividualAlbumDTO(List <IndividualAlbum> albums) {
        List <BasicIndividualAlbumDTO> basicIndividualAlbumDTOS = new ArrayList <>();
        albums.forEach((pd -> basicIndividualAlbumDTOS.add(individualAlbumToBasicIndividualAlbumDTO().map(pd , BasicIndividualAlbumDTO.class))));
        return basicIndividualAlbumDTOS;
    }


    public static BasicIndividualAlbumDTO mapindividualAlbumToBasicIndividualAlbumDTO(IndividualAlbum album) {
        BasicIndividualAlbumDTO basicIndividualAlbumDTO;
        basicIndividualAlbumDTO = individualAlbumToBasicIndividualAlbumDTO().map(album , BasicIndividualAlbumDTO.class);
        return basicIndividualAlbumDTO;
    }
}

