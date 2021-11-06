package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String albumMainPhoto;
    private String albumBackgroundPhoto;
    private String description;
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    private UsersGroup group;

    @OneToMany(
            mappedBy = "album",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupPhoto> photos = new HashSet <>();


    @OneToMany(
            mappedBy = "album",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupAlbumHistory> history = new HashSet <>();

    @OneToOne
    private PersonalData owner;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Coordinates coordinate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public GroupAlbum(PersonalData user , Coordinates coordinate , UsersGroup group , String description, String name) {
        this.owner = user;
        this.coordinate = coordinate;
        this.group = group;
        this.description = description;
        this.dateTime = LocalDateTime.now();
        this.name = name;
    }
}
