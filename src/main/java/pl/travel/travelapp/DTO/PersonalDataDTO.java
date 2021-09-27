package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.Country;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.PersonalDescription;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataDTO {
    private long id;
    private String firstName;
    private String surName;
    private long phoneNumber;
    private String profilePicture;
    private String backgroundPicture;
    private LocalDate birthday;
    private Country nationality;
    private PersonalDescription personalDescription;

    public PersonalDataDTO(PersonalData user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.surName = user.getSurName();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePicture = user.getProfilePicture();
        this.backgroundPicture = user.getBackgroundPicture();
        this.birthday = user.getBirthDate();
        this.nationality = user.getNationality();
        this.personalDescription = user.getPersonalDescription();
    }

}
