package pl.travel.travelapp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import pl.travel.travelapp.entites.Coordinates;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.entites.SharedAlbum;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualAlbumDTO {
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

    public IndividualAlbumDTO(IndividualAlbum album){
        this.id = album.getId();
        this.name = album.getName();
        this.description = album.getDescription();
        this.mainPhoto = album.getMainPhoto();
        this.coordinate = album.getCoordinate();
        this.personalInformationDTO = new PersonalInformationDTO(album.getOwner());
        this.isPublic = album.isPublic();
        this.sharedAlbumList = album.getSharedAlbum();
        this.dateTime = album.getDateTime();
    }

}
