package pl.travel.travelapp.DTO.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.GroupPhoto;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupPhotoAlbumEnterDTO {

    private Long id;
    private PersonalInformationDTO creator;
    private String photo;

    public GroupPhotoAlbumEnterDTO(GroupPhoto photo) {
        this.id = photo.getId();
        this.creator = new PersonalInformationDTO(photo.getOwner());
        this.photo = photo.getPhoto();
    }
}
