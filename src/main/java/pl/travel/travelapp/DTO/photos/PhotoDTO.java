package pl.travel.travelapp.DTO.photos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.Comments;
import pl.travel.travelapp.entites.TaggedUser;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
    private long photoId;
    private String photoUrl;
    private String description;
    private List <Comments> photoComments;
    private Set <TaggedUser> taggedList;

    public PhotoDTO(AlbumPhotos photo) {
        this.photoId = photo.getPhotoId();
        this.photoUrl = photo.getPhotoUrl();
        this.description = photo.getDescription();
        this.photoComments = photo.getComments();
        this.taggedList = photo.getTaggedList();
    }

}
