package pl.travel.travelapp.entites;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class PersonalDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(max = 800)
    private String interest;
    @Size(max = 800)
    private String about;
    private String visitedCountries;

}
