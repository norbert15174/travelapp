package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.NotificationDTO;
import pl.travel.travelapp.entites.FriendsRequest;
import pl.travel.travelapp.entites.SharedAlbum;
import pl.travel.travelapp.entites.TaggedUser;
import pl.travel.travelapp.interfaces.NewsInterfaces;
import pl.travel.travelapp.repositories.SharedAlbumRepository;
import pl.travel.travelapp.repositories.TaggedPhotoRepository;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService implements NewsInterfaces {

    private final IIndividualAlbumQueryService individualAlbumQueryService;
    private final IPersonalQueryService personalQueryService;
    private final SharedAlbumRepository sharedAlbumRepository;
    private final TaggedPhotoRepository taggedPhotoRepository;
    private final FriendsRequestService friendsRequestService;

    @Autowired
    public NewsService(IIndividualAlbumQueryService individualAlbumQueryService , IPersonalQueryService personalQueryService , SharedAlbumRepository sharedAlbumRepository , TaggedPhotoRepository taggedPhotoRepository , FriendsRequestService friendsRequestService) {
        this.individualAlbumQueryService = individualAlbumQueryService;
        this.personalQueryService = personalQueryService;
        this.sharedAlbumRepository = sharedAlbumRepository;
        this.taggedPhotoRepository = taggedPhotoRepository;
        this.friendsRequestService = friendsRequestService;
    }

    @Override
    @Transactional
    public ResponseEntity <IndividualAlbumDTO> getActualNews(Integer page , Principal principal) {
        Long userId = personalQueryService.getPersonalInformation(principal.getName()).getId();
        return new ResponseEntity(individualAlbumQueryService.getIndividualAlbumsNews(userId , PageRequest.of(page , 8)) , HttpStatus.OK);
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void fill() {
        List <SharedAlbum> sharedAlbums = sharedAlbumRepository.findAll();
        List <TaggedUser> taggedUsers = taggedPhotoRepository.findAll();
        Integer i = 0;
        for (SharedAlbum e : sharedAlbums) {
            e.setDateTime(LocalDateTime.now().minusMinutes(i));
            i += 4;
            sharedAlbumRepository.save(e);
        }
        for (TaggedUser e : taggedUsers) {
            e.setDateTime(LocalDateTime.now().minusMinutes(i));
            i--;
            taggedPhotoRepository.save(e);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity <List <NotificationDTO>> getUserNotification(Principal principal) {
        Long userId = personalQueryService.getPersonalInformation(principal.getName()).getId();
        List <FriendsRequest> friendsRequests = friendsRequestService.findRequestsByPrincipalList(principal);
        List <SharedAlbum> sharedAlbums = sharedAlbumRepository.findAvailableAlbumsPage(userId , PageRequest.of(0 , 10));
        List <TaggedUser> taggedUsers = taggedPhotoRepository.findAllPageByUserId(userId , PageRequest.of(0 , 10));
        List <NotificationDTO> notifications = new ArrayList <>();
        notifications.addAll(friendsRequests.stream().map(NotificationDTO::new).collect(Collectors.toList()));
        notifications.addAll(sharedAlbums.stream().map(NotificationDTO::new).collect(Collectors.toList()));
        notifications.addAll(taggedUsers.stream().map(NotificationDTO::new).collect(Collectors.toList()));
        return new ResponseEntity(notifications.stream().sorted(Comparator.comparing(NotificationDTO::getDateTime).reversed()).collect(Collectors.toList()) , HttpStatus.OK);
    }


}
