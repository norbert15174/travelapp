package pl.travel.travelapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class IndividualAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @OneToOne(fetch = FetchType.EAGER)
    private Coordinates coordinate;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData owner;
    private boolean isPublic = false;
    @OneToMany(mappedBy = "individualAlbum", fetch = FetchType.LAZY)
    private List<SharedAlbum> sharedAlbum = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    private List<AlbumPhotos> photos = new ArrayList<>();

}
