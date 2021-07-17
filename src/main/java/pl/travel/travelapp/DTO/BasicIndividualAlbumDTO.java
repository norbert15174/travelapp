package pl.travel.travelapp.DTO;

import lombok.*;
import pl.travel.travelapp.models.Coordinates;

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
}
