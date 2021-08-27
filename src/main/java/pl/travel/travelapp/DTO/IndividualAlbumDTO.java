package pl.travel.travelapp.DTO;

import lombok.*;
import pl.travel.travelapp.entites.Coordinates;
import pl.travel.travelapp.entites.SharedAlbum;

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
}
