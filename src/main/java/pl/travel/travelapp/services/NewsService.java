package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.NotificationDTO;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.entites.FriendsRequest;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.interfaces.NewsInterfaces;
import pl.travel.travelapp.repositories.CommentsRepository;
import pl.travel.travelapp.repositories.SharedAlbumRepository;
import pl.travel.travelapp.repositories.TaggedPhotoRepository;
import pl.travel.travelapp.services.query.interfaces.IIndividualAlbumQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;

import java.security.Principal;
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
    private final CommentsRepository commentsRepository;

    @Autowired
    public NewsService(IIndividualAlbumQueryService individualAlbumQueryService , IPersonalQueryService personalQueryService , SharedAlbumRepository sharedAlbumRepository , TaggedPhotoRepository taggedPhotoRepository , FriendsRequestService friendsRequestService , CommentsRepository commentsRepository) {
        this.individualAlbumQueryService = individualAlbumQueryService;
        this.personalQueryService = personalQueryService;
        this.sharedAlbumRepository = sharedAlbumRepository;
        this.taggedPhotoRepository = taggedPhotoRepository;
        this.friendsRequestService = friendsRequestService;
        this.commentsRepository = commentsRepository;
    }

    @Override
    @Transactional
    public ResponseEntity <List <IndividualAlbumDTO>> getActualNews(Integer page , Principal principal) {
        Long userId = personalQueryService.getPersonalInformation(principal.getName()).getId();
        return new ResponseEntity(individualAlbumQueryService.getIndividualAlbumsNews(userId , PageRequest.of(page , 8)) , HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity <List <NotificationDTO>> getUserNotification(Principal principal) {
        Long userId = personalQueryService.getPersonalInformation(principal.getName()).getId();
        List <FriendsRequest> friendsRequests = friendsRequestService.findRequestsByPrincipalList(principal);
        List <IndividualAlbum> sharedAlbums = sharedAlbumRepository.findIndividualAlbumAvailableAlbumsPage(userId , PageRequest.of(0 , 10));
        List <AlbumPhotos> albumPhotos = taggedPhotoRepository.findAllPageByUserId(userId , PageRequest.of(0 , 10));
        List <AlbumPhotos> albumPhotosComment = commentsRepository.findPhotoUserComment(userId , PageRequest.of(0 , 10));
        List <NotificationDTO> comments = new ArrayList <>();
        albumPhotosComment.forEach(photo -> {
            comments.addAll(photo.getComments().stream().filter(comment -> !comment.getUserId().equals(userId)).map(comment -> new NotificationDTO(comment , photo)).collect(Collectors.toList()));
        });
        List <NotificationDTO> notifications = new ArrayList <>();
        notifications.addAll(friendsRequests.stream().map(NotificationDTO::new).collect(Collectors.toList()));
        notifications.addAll(sharedAlbums.stream().map(album -> new NotificationDTO(album , userId)).collect(Collectors.toList()));
        notifications.addAll(albumPhotos.stream().map(photo -> new NotificationDTO(photo , userId)).collect(Collectors.toList()));
        List <NotificationDTO> commentsToSend = comments.stream().sorted(Comparator.comparing(NotificationDTO::getDateTime).reversed()).collect(Collectors.toList());
        Integer maxSize = commentsToSend.size();
        if ( maxSize > 10 ) {
            notifications.addAll(commentsToSend.subList(0 , 10));
        } else {
            notifications.addAll(commentsToSend);
        }
        return new ResponseEntity(notifications.stream().sorted(Comparator.comparing(NotificationDTO::getDateTime).reversed()).collect(Collectors.toList()) , HttpStatus.OK);
    }


}
