package pl.travel.travelapp.services.query;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.query.interfaces.IUserQueryService;
import pl.travel.travelapp.specification.UserSpecification;
import pl.travel.travelapp.specification.criteria.UserSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PersonalQueryService implements IPersonalQueryService {

    private final IUserQueryService userQueryService;
    private final PersonalDataRepository personalDataRepository;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    public PersonalQueryService(IUserQueryService userQueryService , PersonalDataRepository personalDataRepository) {
        this.userQueryService = userQueryService;
        this.personalDataRepository = personalDataRepository;
    }

    @Override
    public PersonalData getPersonalInformation(String username) {
        return userQueryService.findUserByUsernameAllInformation(username).getPersonalData();
    }

    @Override
    public Optional <PersonalData> findById(Long id) {
        return personalDataRepository.findById(id);
    }

    @Override
    public PersonalData getById(Long id) throws NotFoundException {
        return findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List <PersonalData> findAllById(List <Long> ids) {
        return personalDataRepository.findAllById(ids);
    }

    @Override
    public Set <PersonalData> findAllById(Set <Long> ids) {
        return Sets.newHashSet(personalDataRepository.findAllById(ids));
    }

    @Override
    public PersonalData getPersonalInformationWithAlbums(String username) {
        Long id = getPersonalInformation(username).getId();
        return personalDataRepository.findPersonalDataWithAlbumsByUserId(id);
    }

    @Override
    public Set <PersonalData> getUsersByIds(Set <Long> usersToTag) {
        return personalDataRepository.getUsersByIds(usersToTag);
    }

    @Override
    public List <PersonalInformationDTO> getUsersBySearchCriteria(UserSearchCriteria criteria , Integer page) {
        Specification <PersonalData> filter = new UserSpecification().getFilter(criteria);
        Page <PersonalData> userPage = personalDataRepository.findAll(filter , PageRequest.of(page , PAGE_SIZE));
        return userPage.stream().map(PersonalInformationDTO::new).collect(Collectors.toList());
    }

}
