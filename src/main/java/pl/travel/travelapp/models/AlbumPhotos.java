package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private List <Comments> comments = new ArrayList <>();
}
