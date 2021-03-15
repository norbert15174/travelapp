package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class PersonalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Min(3)
    private String firstName;
    @Min(3)
    private String surName;
    @Email
    private String email;
    @Min(9)
    private long phoneNumber;
    private String profilePicture;
    private LocalDate BirthDate;
    //private Country Nationality;
    //private PersonalDescription personalDescription;
    //private List<Friends> friends = new ArrayList();
    //private List<Group> groups = new ArrayList();
    //private List<Comment> comments = new ArrayList();
    //private List<Album> albums = new ArrayList();

}
