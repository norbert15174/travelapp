package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDate BirthDate;
    @OneToOne
    private Country Nationality;
    //private PersonalDescription personalDescription;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<LinkFriends> friends = new ArrayList<>();
    //private List<Group> groups = new ArrayList();
    //private List<Comment> comments = new ArrayList();
    //private List<Album> albums = new ArrayList();

}
