package pl.travel.travelapp.DTO;

import lombok.*;
import pl.travel.travelapp.models.AlbumPhotos;
import pl.travel.travelapp.models.Coordinates;

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

}
