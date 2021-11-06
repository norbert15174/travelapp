package pl.travel.travelapp.entites;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.groups.CoordinatesDTO;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lang;
    private Double lat;
    private String place;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE , CascadeType.DETACH})
    private Country country;

    public Coordinates(CoordinatesDTO coordinates , Country country) {
        this.country = country;
        this.lang = coordinates.getLang();
        this.lat = coordinates.getLat();
        this.place = coordinates.getPlace();
    }


}
