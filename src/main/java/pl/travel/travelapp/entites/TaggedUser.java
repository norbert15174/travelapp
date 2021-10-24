package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
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
    @Column(columnDefinition = "TIMESTAMP")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private AlbumPhotos albumPhoto;

    public Set <TaggedUser> buildTaggedUsers(Set <PersonalData> users , AlbumPhotos photo) {
        return users.stream().map(user -> new TaggedUser().buildTaggedUser(user , photo)).collect(Collectors.toSet());
    }

    public TaggedUser buildTaggedUser(PersonalData user , AlbumPhotos photo) {
        this.userId = user.getId();
        this.name = user.getFirstName();
        this.surName = user.getSurName();
        this.photo = user.getProfilePicture();
        this.dateTime = LocalDateTime.now();
        this.albumPhoto = photo;
        return this;
    }

    public TaggedUser buildTaggedUser(PersonalData user) {
        this.userId = user.getId();
        this.name = user.getFirstName();
        this.surName = user.getSurName();
        this.photo = user.getProfilePicture();
        this.dateTime = LocalDateTime.now();
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTaggedId());
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null ) return false;
        if ( this.getClass() != o.getClass() ) return false;
        TaggedUser user = (TaggedUser) o;
        return getTaggedId() == user.getTaggedId();
    }

}
