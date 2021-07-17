package pl.travel.travelapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class AlbumPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long photoId;
    private String photoUrl;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private IndividualAlbum individualAlbum;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "commentId")
    private List <Comments> comments = new ArrayList <>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "taggedId")
    private List <TaggedUser> taggedList = new ArrayList <>();
}
