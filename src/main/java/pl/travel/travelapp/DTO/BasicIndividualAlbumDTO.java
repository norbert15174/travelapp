package pl.travel.travelapp.DTO;

import lombok.*;
import pl.travel.travelapp.models.Coordinates;
import pl.travel.travelapp.models.IndividualAlbum;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BasicIndividualAlbumDTO {
    private long id;
    private String name;
    private String description;
    private String mainPhoto;
    private Coordinates coordinate;
    private boolean isPublic;

    public BasicIndividualAlbumDTO(IndividualAlbum individualAlbum){
        this.id = individualAlbum.getId();
        this.name = individualAlbum.getName();
        this.description = individualAlbum.getDescription();
        this.mainPhoto = individualAlbum.getMainPhoto();
        this.coordinate = individualAlbum.getCoordinate();
        this.isPublic = individualAlbum.isPublic();
    }

}
