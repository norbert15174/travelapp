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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupAlbumFullDTO {

    private Long id;
    private PersonalInformationDTO albumOwner;
    private PersonalInformationDTO groupOwner;
    private List <GroupPhotoDTO> photos = new ArrayList <>();
    private String description;
    private String name;
    private String mainPhoto;
    private String backgroundPhoto;
    private Coordinates coordinate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public GroupAlbumFullDTO(GroupAlbum album) {
        this.id = album.getId();
        this.albumOwner = new PersonalInformationDTO(album.getOwner());
        this.groupOwner = new PersonalInformationDTO(album.getGroup().getOwner());
        if ( !album.getPhotos().isEmpty() ) {
            this.photos.addAll(album.getPhotos().stream().map(GroupPhotoDTO::new).collect(Collectors.toList()));
        }
        this.name = album.getName();
        this.description = album.getDescription();
        this.mainPhoto = album.getAlbumMainPhoto();
        this.backgroundPhoto = album.getAlbumBackgroundPhoto();
        this.coordinate = album.getCoordinate();
        this.time = album.getDateTime();
    }

}
