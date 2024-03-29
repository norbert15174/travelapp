package pl.travel.travelapp.DTO.groups;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GroupAlbumCreateDTO {

    private String description;
    private String name;
    private CoordinatesDTO coordinates;
    private Long newOwnerId;

    public boolean canBeSave() {
        return !Strings.isNullOrEmpty(description) &&
                !Strings.isNullOrEmpty(name) &&
                coordinates != null &&
                !Strings.isNullOrEmpty(coordinates.getCountry()) &&
                !(coordinates.getLang() > 180 || coordinates.getLang() < -180) &&
                !(coordinates.getLat() > 90 || coordinates.getLat() < -90) &&
                !Strings.isNullOrEmpty(coordinates.getPlace());
    }

    public boolean hasValidCooridantes() {
        return coordinates != null &&
                !Strings.isNullOrEmpty(coordinates.getCountry()) &&
                !(coordinates.getLang() > 180 || coordinates.getLang() < -180) &&
                !(coordinates.getLat() > 90 || coordinates.getLat() < -90) &&
                !Strings.isNullOrEmpty(coordinates.getPlace());
    }

    public boolean hasName() {
        return !Strings.isNullOrEmpty(name);
    }

    public boolean hasDescription() {
        return !Strings.isNullOrEmpty(description);
    }


}
