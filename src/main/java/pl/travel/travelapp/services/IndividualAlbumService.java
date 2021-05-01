package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.IndividualAlbumRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

@Service
public class IndividualAlbumService {
    private IndividualAlbumRepository individualAlbumRepository;
    private PersonalDataRepository personalDataRepository;
    @Autowired
    public IndividualAlbumService(IndividualAlbumRepository individualAlbumRepository , PersonalDataRepository personalDataRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
        this.personalDataRepository = personalDataRepository;
    }

}
