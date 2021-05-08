package pl.travel.travelapp.builders;

import pl.travel.travelapp.DTO.PersonalInformationDTO;

public class PersonalInformationDTOBuilder {
    private long id;
    private String name;
    private String surName;
    private String photo;

    public PersonalInformationDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public PersonalInformationDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonalInformationDTOBuilder setSurName(String surName) {
        this.surName = surName;
        return this;
    }

    public PersonalInformationDTOBuilder setPhoto(String photo) {
        this.photo = photo;
        return this;
    }
    public PersonalInformationDTO createPersonalInformationDTO(){
        return new PersonalInformationDTO(id,name,surName,photo);
    }
}
