package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.models.enums.SharedAlbumStatus;

import javax.persistence.*;

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
    private SharedAlbumStatus status;
    @ManyToOne
    private IndividualAlbum individualAlbum;
}
