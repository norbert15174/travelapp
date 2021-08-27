package pl.travel.travelapp.entites;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(max = 50)
    private String name;
    @Size(max = 800)
    private String description;
    private String mainPhoto;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Coordinates coordinate;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData owner;
    private boolean isPublic = false;
    @OneToMany(mappedBy = "individualAlbum",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<SharedAlbum> sharedAlbum = new ArrayList<>();
    @OneToMany(mappedBy = "individualAlbum",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AlbumPhotos> photos = new ArrayList<>();

    public void addNewUserToAlbumShare(SharedAlbum sharedAlbum){
        boolean isExist = this.sharedAlbum.stream().anyMatch(shared -> sharedAlbum.getUserId() == shared.getUserId());
        if(isExist) return;
        this.sharedAlbum.add(sharedAlbum);
        sharedAlbum.setIndividualAlbum(this);
    }

    public void deleteUserFromAlbumShare(SharedAlbum sharedAlbum){
        this.sharedAlbum.remove(sharedAlbum);
    }

    public void addNewPhoto(AlbumPhotos albumPhotos){
        this.photos.add(albumPhotos);
    }

    public void deletePhoto(AlbumPhotos albumPhotos){
        this.photos.remove(albumPhotos);
    }

}