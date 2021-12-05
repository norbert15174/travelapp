package pl.travel.travelapp.specification.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AlbumSearchCriteria {
    private String name;
    private String ownerName;
    private String country;
    private String place;
    @JsonIgnore
    private Long userId;
}
