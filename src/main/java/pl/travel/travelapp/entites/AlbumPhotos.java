package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class AlbumPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long photoId;
    private String photoUrl;
    @Size(max = 1000)
    private String description;
    private String photoName;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private IndividualAlbum individualAlbum;
    @Size(max = 500)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "photoId")
    private List <Comments> comments = new ArrayList <>();
    @OneToMany(
            mappedBy = "albumPhoto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <TaggedUser> taggedList = new HashSet <>();

    public void deletePhotoFromAlbum(AlbumPhotos albumPhotos) {
        individualAlbum.deletePhoto(albumPhotos);
    }

    public void addComment(Comments comment) {
        this.comments.add(comment);
    }

    public void deleteComment(Comments comment) {
        this.comments.remove(comment);
    }

    public void addTaggedUser(TaggedUser taggedUser) {
        this.taggedList.add(taggedUser);
    }

    public void addTaggedUser(Set <TaggedUser> taggedUser) {
        this.taggedList.addAll(taggedUser);
    }

    public void deleteTaggedUser(TaggedUser taggedUser) {
        this.taggedList.remove(taggedUser);
    }

    public void deleteTaggedUser(Set <TaggedUser> taggedUser) {
        this.taggedList.remove(taggedUser);
    }

}
