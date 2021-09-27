package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.enums.SharedAlbumStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(columnDefinition = "TIMESTAMP")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public SharedAlbum build(PersonalData personalData) {
        this.name = personalData.getFirstName();
        this.surName = personalData.getSurName();
        this.photo = personalData.getProfilePicture();
        this.userId = personalData.getId();
        this.dateTime = LocalDateTime.now();
        return this;
    }

}
