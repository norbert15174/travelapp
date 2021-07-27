package pl.travel.travelapp.builders;

import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.models.Coordinates;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.SharedAlbum;

import java.util.List;

public class IndividualAlbumDTOBuilder {
    private long id;
    private String name;
    private String description;
    private String mainPhoto;
    private Coordinates coordinate;
    private PersonalInformationDTO personalInformationDTO;
    private boolean isPublic;
    private List <SharedAlbum> sharedAlbumList;

    public IndividualAlbumDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public IndividualAlbumDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public IndividualAlbumDTOBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public IndividualAlbumDTOBuilder setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
        return this;
    }

    public IndividualAlbumDTOBuilder setCoordinate(Coordinates coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public IndividualAlbumDTOBuilder setPersonalInformationDTO(PersonalInformationDTO personalInformationDTO) {
        this.personalInformationDTO = personalInformationDTO;
        return this;
    }

    public IndividualAlbumDTOBuilder setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }
    public IndividualAlbumDTOBuilder setSharedAlbum(List<SharedAlbum> sharedAlbum) {
        sharedAlbumList = sharedAlbum;
        return this;
    }

    public IndividualAlbumDTO createIndividualAlbumDTO(){
        return new IndividualAlbumDTO(id,name,description,mainPhoto,coordinate,personalInformationDTO,isPublic,sharedAlbumList);
    }
}
