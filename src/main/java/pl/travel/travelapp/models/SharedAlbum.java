package pl.travel.travelapp.models;

import javax.persistence.*;

@Entity
public class SharedAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String name;
    private String surName;
    private String photo;
    @ManyToOne
    private IndividualAlbum individualAlbum;
}
