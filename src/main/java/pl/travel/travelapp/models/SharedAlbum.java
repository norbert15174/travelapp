package pl.travel.travelapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.models.enums.SharedAlbumStatus;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SharedAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String name;
    private String surName;
    private String photo;
    @Enumerated(EnumType.STRING)
    private SharedAlbumStatus status = SharedAlbumStatus.NEW;
    @ManyToOne
    @JsonIgnore
    private IndividualAlbum individualAlbum;

    public SharedAlbum build(PersonalData personalData) {
        this.name = personalData.getFirstName();
        this.surName = personalData.getSurName();
        this.photo = personalData.getProfilePicture();
        this.userId = personalData.getId();
        return this;
    }

}
