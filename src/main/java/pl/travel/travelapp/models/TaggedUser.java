package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;
import java.util.stream.Collectors;

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


    public Set <TaggedUser> buildTaggedUsers(Set <PersonalData> users) {
        return users.stream().map(user -> new TaggedUser().buildTaggedUser(user)).collect(Collectors.toSet());
    }

    public TaggedUser buildTaggedUser(PersonalData user) {
        this.userId = user.getId();
        this.name = user.getFirstName();
        this.surName = user.getSurName();
        this.photo = user.getProfilePicture();
        return this;
    }

}
