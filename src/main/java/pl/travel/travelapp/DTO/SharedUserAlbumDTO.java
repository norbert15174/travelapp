package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.SharedAlbum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SharedUserAlbumDTO {
    private long id;
    private String name;
    private String surName;
    private String photo;
    private Long userId;

    public SharedUserAlbumDTO (SharedAlbum user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surName = user.getSurName();
        this.photo = user.getPhoto();
        this.userId = user.getUserId();
    }
}
