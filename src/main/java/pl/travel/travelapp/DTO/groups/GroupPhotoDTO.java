package pl.travel.travelapp.DTO.groups;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.GroupPhoto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupPhotoDTO {
    private long photoId;
    private String photoUrl;
    private String description;
    private List <GroupCommentsDTO> photoComments = new ArrayList <>();
    private Set <GroupTaggedDTO> taggedList = new HashSet <>();
    private PersonalInformationDTO owner;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    public GroupPhotoDTO(GroupPhoto photo) {
        this.photoId = photo.getId();
        this.photoUrl = photo.getPhoto();
        this.description = photo.getDescription();
        if ( !photo.getComments().isEmpty() ) {
            this.photoComments.addAll(photo.getComments()
                    .stream()
                    .map(comment -> new GroupCommentsDTO(comment))
                    .sorted(Comparator.comparing(GroupCommentsDTO::getTime).reversed())
                    .collect(Collectors.toList()));
        }
        if ( !photo.getTagged().isEmpty() ) {
            this.taggedList.addAll(photo.getTagged().stream().map(GroupTaggedDTO::new).collect(Collectors.toSet()));
        }
        this.owner = new PersonalInformationDTO(photo.getOwner());
        this.dateTime = photo.getDateTime();
    }

}
