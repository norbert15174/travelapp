package pl.travel.travelapp.services;

import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.builders.FriendsBuilder;
import pl.travel.travelapp.builders.FriendsRequestBuilder;
import pl.travel.travelapp.interfaces.FriendsRequestInterface;
import pl.travel.travelapp.mappers.FriendsRequestObjectMapperClass;
import pl.travel.travelapp.models.Friends;
import pl.travel.travelapp.models.FriendsRequest;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.repositories.FriendsRequestRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsRequestService implements FriendsRequestInterface {

    private FriendsRequestRepository friendsRequestRepository;
    private PersonalService personalService;
    private PersonalDataRepository personalDataRepository;
    private FriendsRepository friendsRepository;

    @Autowired
    public FriendsRequestService(FriendsRequestRepository friendsRequestRepository , PersonalService personalService , PersonalDataRepository personalDataRepository , FriendsRepository friendsRepository) {
        this.friendsRequestRepository = friendsRequestRepository;
        this.personalService = personalService;
        this.personalDataRepository = personalDataRepository;
        this.friendsRequestRepository = friendsRequestRepository;
        this.friendsRepository = friendsRepository;
    }


    @Override
    @Transactional
    public ResponseEntity <FriendsRequest> addUserToFriendsWaitingList(Principal principal , long id) {
        PersonalData user = getPersonalInformation(principal);
        if ( user.getId() == id ) return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        if ( !personalDataRepository.findById(id).isPresent() ) return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        if ( !friendsRequestRepository.findFirstByReceiver(id , user.getId()).isEmpty() || friendsRepository.findFriendByFirstAndSecond(id , user.getId()).isPresent() )
            return new ResponseEntity <>(HttpStatus.CONFLICT);

        try {
            friendsRequestRepository
                    .save(new FriendsRequestBuilder()
                            .setReceiver(id)
                            .setSender(user)
                            .createFriendsRequest());
            return new ResponseEntity <>(HttpStatus.CREATED);
        } catch ( Exception e ) {
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }

    }

    @Override
    public ResponseEntity deleteRequest(Principal principal , long id) {
        PersonalData user = getPersonalInformation(principal);
        if ( friendsRequestRepository.findFirstByReceiver(id , user.getId()).isEmpty() )
            return new ResponseEntity <>(HttpStatus.CONFLICT);
        try {
            Optional <FriendsRequest> friendsRequest = friendsRequestRepository.findById(id);
            if ( friendsRequest.isEmpty() ) return new ResponseEntity <>(HttpStatus.NOT_FOUND);
            if ( friendsRequest.get().getReceiver() != user.getId() )
                return new ResponseEntity <>(HttpStatus.FORBIDDEN);
            if ( deleteFriendRequest(id) ) return new ResponseEntity(HttpStatus.OK);
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        } catch ( Exception e ) {
            System.err.println(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @Transactional
    public boolean deleteFriendRequest(long id) {
        try {
            friendsRequestRepository.deleteById(id);
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    public ResponseEntity <List <UserFriendRequestDTO>> findRequestsByPrincipal(Principal principal) {
        try {
            PersonalData user = getPersonalInformation(principal);
            Optional <List <FriendsRequest>> userFriendRequest = friendsRequestRepository.findAllUserInvitations(user.getId());
            if ( userFriendRequest.isEmpty() ) return new ResponseEntity <>(HttpStatus.NO_CONTENT);
            return new ResponseEntity <>(FriendsRequestObjectMapperClass.mapPersonalDataToPersonalDataDTO(userFriendRequest.get()) , HttpStatus.OK);
        } catch ( NullPointerException | MappingException e ) {
            return new ResponseEntity <>(HttpStatus.REQUEST_TIMEOUT);
        }
    }

    @Transactional
    public ResponseEntity <Friends> acceptToFriendsList(Principal principal , long id) {
        PersonalData user = getPersonalInformation(principal);
        Optional <FriendsRequest> friendsRequest = friendsRequestRepository.findFriendsRequest(id);
        if ( friendsRequest.isPresent() ) {
            FriendsRequest friends = friendsRequest.get();
            if ( friends.getReceiver() != user.getId() ) return new ResponseEntity <>(HttpStatus.FORBIDDEN);

            Friends friendsToSave = new FriendsBuilder()
                    .setFirstUser(user)
                    .setSecondUser(friends.getSender())
                    .createFriends();
            friendsRepository.save(friendsToSave);
            friends.setFriends(true);
            friendsRequestRepository.deleteById(friends.getId());
            return new ResponseEntity <>(HttpStatus.OK);
        } else {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public boolean checkIfRequestIsCorrect(Principal principal , long id) {
        Optional <FriendsRequest> friendsRequest = friendsRequestRepository.findById(id);
        if ( friendsRequest.isEmpty() ) return false;
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        return friendsRequest.get().getReceiver() == user.getId();
    }

    private PersonalData getPersonalInformation(Principal principal) {
        return personalService.getPersonalInformation(principal.getName());
    }


}
