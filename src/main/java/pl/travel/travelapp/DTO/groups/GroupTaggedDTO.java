package pl.travel.travelapp.DTO.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.GroupPhotoTagged;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupTaggedDTO {

    private long taggedId;
    private long userId;
    private String name;
    private String surName;
    private String photo;

    public GroupTaggedDTO(GroupPhotoTagged tagged) {
        this.taggedId = tagged.getId();
        this.userId = tagged.getTagged().getId();
        this.name = tagged.getTagged().getFirstName();
        this.surName = tagged.getTagged().getSurName();
        this.photo = tagged.getTagged().getProfilePicture();
    }
}
