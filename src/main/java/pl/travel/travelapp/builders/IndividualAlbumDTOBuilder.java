package pl.travel.travelapp.builders;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.Coordinates;
import pl.travel.travelapp.entites.SharedAlbum;

import java.time.LocalDateTime;
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

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

    public IndividualAlbumDTOBuilder setSharedAlbum(List <SharedAlbum> sharedAlbum) {
        sharedAlbumList = sharedAlbum;
        return this;
    }

    public IndividualAlbumDTOBuilder setDate(LocalDateTime date) {
        this.dateTime = date;
        return this;
    }

    public IndividualAlbumDTO createIndividualAlbumDTO() {
        return new IndividualAlbumDTO(id , name , description , mainPhoto , coordinate , personalInformationDTO , isPublic , sharedAlbumList , dateTime);
    }
}
