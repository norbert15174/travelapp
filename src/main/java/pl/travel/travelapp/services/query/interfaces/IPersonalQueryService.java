package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.entites.Friends;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IPersonalQueryService {

    PersonalData getPersonalInformation(String username);

    Optional <PersonalData> findById(Long id);

    PersonalData getById(Long id) throws NotFoundException;

    List <PersonalData> findAllById(List <Long> ids);

    Set <PersonalData> findAllById(Set <Long> ids);

    PersonalData getPersonalInformationWithAlbums(String username);

    Set<PersonalData> getUsersByIds(Set<Long> usersToTag);
}
