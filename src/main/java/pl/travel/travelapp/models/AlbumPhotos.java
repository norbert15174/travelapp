package pl.travel.travelapp.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AlbumPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String photoUrl;
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private List <Comments> comments = new ArrayList <>();
}
