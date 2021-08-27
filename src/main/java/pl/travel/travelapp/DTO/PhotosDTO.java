package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.Comments;
import pl.travel.travelapp.entites.TaggedUser;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotosDTO {
    private long photoId;
    private String photoUrl;
    private String description;
    private List <Comments> photoComments;
    private Set <TaggedUser> taggedList;
}
