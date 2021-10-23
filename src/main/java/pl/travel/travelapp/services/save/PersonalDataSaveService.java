package pl.travel.travelapp.services.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.services.save.interfaces.IPersonalDataSaveService;

import java.util.Set;

@Service
public class PersonalDataSaveService implements IPersonalDataSaveService {

    private final PersonalDataRepository personalDataRepository;

    @Autowired
    public PersonalDataSaveService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    @Transactional
    @Override
    public void update(PersonalData user) {
        personalDataRepository.save(user);
    }

    @Transactional
    @Override
    public void updateAll(Set <PersonalData> members) {
        personalDataRepository.saveAll(members);
    }

}
