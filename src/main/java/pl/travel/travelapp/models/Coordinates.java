package pl.travel.travelapp.models;

import javax.persistence.*;

@Entity
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
