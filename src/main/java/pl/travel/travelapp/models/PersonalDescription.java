package pl.travel.travelapp.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

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
    @Size(max = 8000)
    private String interest;
    @Size(max = 500)
    private String about;
    @OneToMany
    private List<Country> visitedCountries;
}
