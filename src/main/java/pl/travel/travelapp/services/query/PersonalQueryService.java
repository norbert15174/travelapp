package pl.travel.travelapp.services.query;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.query.interfaces.IUserQueryService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonalQueryService implements IPersonalQueryService {

    private final IUserQueryService userQueryService;
    private final PersonalDataRepository personalDataRepository;

    @Autowired
    public PersonalQueryService(IUserQueryService userQueryService , PersonalDataRepository personalDataRepository) {
        this.userQueryService = userQueryService;
        this.personalDataRepository = personalDataRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public PersonalData getPersonalInformation(String username) {
        return userQueryService.findUserByUsernameAllInformation(username).getPersonalData();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional <PersonalData> findById(Long id) {
        return personalDataRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonalData getById(Long id) throws NotFoundException {
        return findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public List <PersonalData> findAllById(List <Long> ids) {
        return personalDataRepository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    @Override
    public Set <PersonalData> findAllById(Set <Long> ids) {
        return Sets.newHashSet(personalDataRepository.findAllById(ids));
    }

    @Transactional(readOnly = true)
    @Override
    public PersonalData getPersonalInformationWithAlbums(String username) {
        Long id = getPersonalInformation(username).getId();
        return personalDataRepository.findPersonalDataWithAlbumsByUserId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Set <PersonalData> getUsersByIds(Set <Long> usersToTag) {
        return personalDataRepository.getUsersByIds(usersToTag);
    }

}
