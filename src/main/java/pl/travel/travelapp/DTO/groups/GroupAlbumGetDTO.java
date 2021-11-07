package pl.travel.travelapp.DTO.groups;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.Coordinates;
import pl.travel.travelapp.entites.GroupAlbum;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupAlbumGetDTO {

    private Long id;
    private PersonalInformationDTO albumOwner;
    private String name;
    private String description;
    private String mainPhoto;
    private String backgroundPhoto;
    private Coordinates coordinates;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public GroupAlbumGetDTO(GroupAlbum album) {
        this.id = album.getId();
        this.albumOwner = new PersonalInformationDTO(album.getOwner());
        this.description = album.getDescription();
        this.name = album.getName();
        this.backgroundPhoto = album.getAlbumBackgroundPhoto();
        this.mainPhoto = album.getAlbumMainPhoto();
        this.coordinates = album.getCoordinate();
        this.time = album.getDateTime();
    }

}
