package pl.travel.travelapp.services;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.travel.travelapp.repositories.*;

import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FriendsRequestServiceTest {

    private PersonalDataRepository personalDataRepository = mock(PersonalDataRepository.class);
    private PersonalDescriptionRepository personalDescriptionRepository = mock(PersonalDescriptionRepository.class);
    private CountryRepository countryRepository = mock(CountryRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private FriendsRequestRepository friendsRequestRepository = mock(FriendsRequestRepository.class);
//    private PersonalService personalService = new PersonalService(personalDataRepository,personalDescriptionRepository,countryRepository,userRepository , individualAlbumRepository);
   // private FriendsRequestService friendsRequestService = new FriendsRequestService(friendsRequestRepository,personalService);

//    @Test
//    public void shouldReturnFriendRequestWithCode200(){
//        //given
//        PersonalData personalData = new PersonalData();
//        personalData.setBackgroundPicture("background-image");
//        personalData.setProfilePicture("picture");
//        personalData.setFirstName("Norbert");
//        personalData.setSurName("Faron");
//        personalData.setPhoneNumber(511423123L);
//        personalData.setId(1);
//        User user = new User();
//        user.setLogin("axe89");
//        user.setPersonalData(personalData);
//        FriendsRequest friend = new FriendsRequestBuilder().setReceiver(2L).setSender(personalData).createFriendsRequest();
//        //when
//        Mockito.when(userRepository.findPersonalDataByUser("axe89")).thenReturn(user);
//        Mockito.when(friendsRequestRepository.findFirstByReceiver(2L,1L)).thenReturn(Optional.empty());
//        Mockito.when(friendsRequestRepository.save(Mockito.any(FriendsRequest.class))).thenReturn(friend);
//
//        //then
//        Principal principal = () -> "axe89";
//
//        try {
//            ResponseEntity<FriendsRequest> friendsRequest = friendsRequestService.addUserToFriendsWaitingList(principal,2L);
//            assertTrue(friendsRequest.getStatusCode().is2xxSuccessful());
//            assertEquals(2L,friendsRequest.getBody().getReceiver());
//            assertEquals(personalData,friendsRequest.getBody().getSender());
//        }catch (NullPointerException e){
//            fail("Method returned NULL");
//        }
//
//    }


}