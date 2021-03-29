package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.builders.FriendsRequestBuilder;
import pl.travel.travelapp.interfaces.FriendsRequestInterface;
import pl.travel.travelapp.models.FriendsRequest;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.FriendsRequestRepository;

import java.security.Principal;
import java.util.List;

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
            return new ResponseEntity<>(friendsRequestRepository.save(new FriendsRequestBuilder().setReceiver(id).setSender(user).createFriendsRequest()),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    @Override
    public ResponseEntity deleteRequest(Principal principal, long id) {
        PersonalData user = personalService.getPersonalInformation(principal.getName());
        if(friendsRequestRepository.findFirstByReceiver(id,user.getId()).isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT);

        return null;
    }

    @Override
    public ResponseEntity<List<UserFriendRequestDTO>> findRequestById(Principal principal) {
        return null;
    }
}
