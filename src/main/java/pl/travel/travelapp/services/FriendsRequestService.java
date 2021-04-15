package pl.travel.travelapp.services;

import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.builders.FriendsBuilder;
import pl.travel.travelapp.builders.FriendsRequestBuilder;
import pl.travel.travelapp.builders.LinkFriendsBuilder;
import pl.travel.travelapp.interfaces.FriendsRequestInterface;
import pl.travel.travelapp.mappers.FriendsRequestObjectMapperClass;
import pl.travel.travelapp.models.*;
import pl.travel.travelapp.repositories.FriendsRepository;
import pl.travel.travelapp.repositories.FriendsRequestRepository;
import pl.travel.travelapp.repositories.LinkFriendsRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsRequestService implements FriendsRequestInterface {

    private FriendsRequestRepository friendsRequestRepository;
    private PersonalService personalService;
    private FriendsRepository friendsRepository;
    private LinkFriendsRepository linkFriendsRepository;
    private PersonalDataRepository personalDataRepository;
    @Autowired
    public FriendsRequestService(FriendsRequestRepository friendsRequestRepository, PersonalService personalService,FriendsRepository friendsRepository,LinkFriendsRepository linkFriendsRepository,PersonalDataRepository personalDataRepository) {
        this.friendsRequestRepository = friendsRequestRepository;
        this.personalService = personalService;
        this.friendsRepository = friendsRepository;
        this.linkFriendsRepository = linkFriendsRepository;
        this.personalDataRepository = personalDataRepository;
    }



    @Override
    @Transactional
    public ResponseEntity<FriendsRequest> addUserToFriendsWaitingList(Principal principal,long id) {
        PersonalData user = getPersonalInformation(principal);
        if(friendsRequestRepository.findFirstByReceiver(id,user.getId()).isPresent()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        try {
            FriendsRequest friendsRequest = new FriendsRequestBuilder().setReceiver(id).setSender(user).createFriendsRequest();
            return new ResponseEntity<>(friendsRequestRepository.save(friendsRequest),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    @Override
    public ResponseEntity deleteRequest(Principal principal, long id) {
        PersonalData user = getPersonalInformation(principal);
        if(friendsRequestRepository.findFirstByReceiver(id,user.getId()).isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        try {
            Optional<FriendsRequest> friendsRequest = friendsRequestRepository.findById(id);
            if(friendsRequest.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if(friendsRequest.get().getReceiver() != user.getId()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            if(deleteFriendRequest(id)) return new ResponseEntity(HttpStatus.OK);
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @Transactional
    public boolean deleteFriendRequest(long id){
        try {
            friendsRequestRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public ResponseEntity<List<UserFriendRequestDTO>> findRequestsByPrincipal(Principal principal) {
        try {
            PersonalData user = getPersonalInformation(principal);
            Optional<List<FriendsRequest>> userFriendRequest = friendsRequestRepository.findAllUserInvitations(user.getId());
            if(userFriendRequest.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(FriendsRequestObjectMapperClass.mapPersonalDataToPersonalDataDTO(userFriendRequest.get()),HttpStatus.OK);
        }catch (NullPointerException | MappingException e){
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
    }
    @Transactional
    public ResponseEntity<Friends> acceptToFriendsList(Principal principal, long id){
        PersonalData user = getPersonalInformation(principal);
        Optional<FriendsRequest> friendsRequest = friendsRequestRepository.findFriendsRequest(id);
        if(friendsRequest.isPresent()){
            FriendsRequest friends = friendsRequest.get();
            if(friends.getReceiver() != user.getId()) return new ResponseEntity <>(HttpStatus.FORBIDDEN);
            //GroupLeader is PersonalData leader id
            Friends linkedFriends = new FriendsBuilder().setMessages(new FriendMessages()).setGroupLeader(friends.getSender().getId()).createFriends();
            friendsRepository.save(linkedFriends);
            LinkFriends friendFirst = new LinkFriendsBuilder().setFriend(linkedFriends).setUser(user).createLinkFriends();
            LinkFriends friendSecond = new LinkFriendsBuilder().setFriend(linkedFriends).setUser(friends.getSender()).createLinkFriends();
            user.addFriend(friendFirst);
            friends.getSender().addFriend(friendSecond);
            personalDataRepository.save(user);
            personalDataRepository.save(friends.getSender());
            linkFriendsRepository.save(friendFirst);
            linkFriendsRepository.save(friendSecond);
            return new ResponseEntity <>(HttpStatus.OK);
        }else {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public boolean checkIfRequestIsCorrect(Principal principal,long id) {
        Optional<FriendsRequest> friendsRequest = friendsRequestRepository.findById(id);
        if(friendsRequest.isEmpty()) return false;
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        return friendsRequest.get().getReceiver() == user.getId();
    }

    private PersonalData getPersonalInformation(Principal principal){
        return personalService.getPersonalInformation(principal.getName());
    }



}
