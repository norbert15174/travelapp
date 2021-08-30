package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.entites.PersonalData;

import java.util.List;
import java.util.Optional;

public interface IPersonalQueryService {

    PersonalData getPersonalInformation(String username);

    Optional <PersonalData> findById(Long id);

    List <PersonalData> findAllById(List <Long> ids);

    PersonalData getPersonalInformationWithAlbums(String username);
}
