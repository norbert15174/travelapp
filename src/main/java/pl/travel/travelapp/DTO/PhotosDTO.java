package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.models.Comments;
import pl.travel.travelapp.models.SharedAlbum;
import pl.travel.travelapp.models.TaggedUser;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotosDTO {
    private long photoId;
    private String photoUrl;
    private String description;
    private List <Comments> photoComments;
    private List<TaggedUser> taggedList;
}
