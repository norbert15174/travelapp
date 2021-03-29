package pl.travel.travelapp.services;

import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.builders.FriendsRequestBuilder;
import pl.travel.travelapp.interfaces.FriendsRequestInterface;
import pl.travel.travelapp.mappers.FriendsRequestObjectMapperClass;
import pl.travel.travelapp.models.FriendsRequest;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.FriendsRequestRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsRequestService implements FriendsRequestInterface {

    private FriendsRequestRepository friendsRequestRepository;
    private PersonalService personalService;
    @Autowired
    public FriendsRequestService(FriendsRequestRepository friendsRequestRepository, PersonalService personalService) {
        this.friendsRequestRepository = friendsRequestRepository;
        this.personalService = personalService;
    }



    @Override
    @Transactional
    public ResponseEntity<FriendsRequest> addUserToFriendsWaitingList(Principal principal,long id) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
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
        PersonalData user = personalService.getPersonalInformation(principal.getName());
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
            PersonalData user = personalService.getPersonalInformation(principal.getName());
            Optional<List<FriendsRequest>> userFriendRequest = friendsRequestRepository.findAllUserInvitations(user.getId());
            if(userFriendRequest.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(FriendsRequestObjectMapperClass.mapPersonalDataToPersonalDataDTO(userFriendRequest.get()),HttpStatus.OK);
        }catch (NullPointerException | MappingException e){
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
    }

    @Override
    public boolean checkIfRequestIsCorrect(Principal principal,long id) {
        Optional<FriendsRequest> friendsRequest = friendsRequestRepository.findById(id);
        if(friendsRequest.isEmpty()) return false;
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        return friendsRequest.get().getReceiver() == user.getId();
    }


}
