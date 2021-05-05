package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double lang;
    private double lat;
    private String place;
    @OneToOne(fetch = FetchType.EAGER)
    private Country country;
}
