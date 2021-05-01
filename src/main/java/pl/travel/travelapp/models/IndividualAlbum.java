package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class IndividualAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    //private Comments comments;
    //private Coordinates coordinate;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData owner;

}
