package pl.travel.travelapp.services.save.interfaces;

import pl.travel.travelapp.entites.PersonalData;

import java.util.Set;

public interface IPersonalDataSaveService {
    void update(PersonalData user);

    void updateAll(Set<PersonalData> members);
}
