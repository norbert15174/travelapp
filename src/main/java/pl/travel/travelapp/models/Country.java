package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String country;
    private String url;
}
