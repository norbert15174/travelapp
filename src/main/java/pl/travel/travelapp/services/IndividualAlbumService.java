package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.interfaces.CoordinateInterface;
import pl.travel.travelapp.interfaces.IndividualAlbumInterface;
import pl.travel.travelapp.interfaces.SharedAlbumInterface;
import pl.travel.travelapp.models.IndividualAlbum;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.*;

@Service
public class IndividualAlbumService implements IndividualAlbumInterface, CoordinateInterface, SharedAlbumInterface {
    private IndividualAlbumRepository individualAlbumRepository;
    private PersonalDataRepository personalDataRepository;
    private CommentsRepository commentsRepository;
    private CoordinatesRepository coordinatesRepository;
    private AlbumPhotosRepository albumPhotosRepository;

    public IndividualAlbumService(IndividualAlbumRepository individualAlbumRepository , PersonalDataRepository personalDataRepository , CommentsRepository commentsRepository , CoordinatesRepository coordinatesRepository , AlbumPhotosRepository albumPhotosRepository) {
        this.individualAlbumRepository = individualAlbumRepository;
        this.personalDataRepository = personalDataRepository;
        this.commentsRepository = commentsRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.albumPhotosRepository = albumPhotosRepository;
    }
}
