package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.models.PersonalData;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformationDTO {
    private long id;
    private String name;
    private String surName;
    private String photo;

    public PersonalInformationDTO (PersonalData user) {
        this.id = user.getId();
        this.name = user.getFirstName();
        this.surName = user.getSurName();
        this.photo = user.getProfilePicture();
    }
}
