package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TaggedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taggedId;
    private long userId;
    private String name;
    private String surName;
    private String photo;
}
