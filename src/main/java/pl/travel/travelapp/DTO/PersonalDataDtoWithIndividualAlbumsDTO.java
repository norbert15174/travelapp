package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.models.IndividualAlbum;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataDtoWithIndividualAlbumsDTO {
    private PersonalDataDTO personalDataDTO;
    private List <BasicIndividualAlbumDTO> individualAlbumDTO;
}
