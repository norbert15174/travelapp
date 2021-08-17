package pl.travel.travelapp.builders;

import lombok.*;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.DTO.PhotosDTO;
import pl.travel.travelapp.models.AlbumPhotos;
import pl.travel.travelapp.models.Coordinates;
import pl.travel.travelapp.models.SharedAlbum;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndividualAlbumFullInformationBuilder {
    private long id;
    private String name;
    private String description;
    private String mainPhoto;
    private Coordinates coordinate;
    private PersonalInformationDTO personalInformationDTO;
    private boolean isPublic;
    private List <SharedAlbum> sharedAlbumList;
    private List <AlbumPhotos> photos;
}
