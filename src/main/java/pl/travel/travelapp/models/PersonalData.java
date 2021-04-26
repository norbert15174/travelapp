package pl.travel.travelapp.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class PersonalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 4 , max = 20)
    private String firstName;
    @Size(min = 4 , max = 20)
    private String surName;
    private long phoneNumber;
    private String profilePicture;
    private String backgroundPicture;
    private LocalDate BirthDate;
    @OneToOne
    private Country Nationality;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PersonalDescription personalDescription;


    //private List<Group> groups = new ArrayList();
    //private List<Comment> comments = new ArrayList();
    //private List<Album> albums = new ArrayList();


}
