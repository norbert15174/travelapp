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
public class GroupPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private String description;

    @OneToOne
    private PersonalData owner;

    @ManyToOne
    private GroupAlbum album;

    @OneToMany(
            mappedBy = "photo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupPhotoTagged> tagged = new HashSet <>();

    @OneToMany(
            mappedBy = "photo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupPhotoComments> comments = new HashSet <>();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public void addTagged(GroupPhotoTagged taggedUser) {
        this.tagged.add(taggedUser);
        taggedUser.setPhoto(this);
    }

    public void untag(Set <GroupPhotoTagged> groupPhotoTaggeds) {
        this.tagged.removeAll(groupPhotoTaggeds);
    }


}
