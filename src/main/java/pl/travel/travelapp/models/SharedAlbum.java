package pl.travel.travelapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.models.enums.SharedAlbumStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class SharedAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String name;
    private String surName;
    private String photo;
    private SharedAlbumStatus status = SharedAlbumStatus.NEW;
    @ManyToOne
    @JsonIgnore
    private IndividualAlbum individualAlbum;


    public SharedAlbum(PersonalData personalData) {
        this.name = personalData.getFirstName();
        this.surName = personalData.getSurName();
        this.photo = personalData.getProfilePicture();
        this.userId = personalData.getId();
    }

}
