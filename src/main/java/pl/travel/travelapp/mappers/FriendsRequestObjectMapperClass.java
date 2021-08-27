package pl.travel.travelapp.mappers;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.travel.travelapp.DTO.UserFriendRequestDTO;
import pl.travel.travelapp.entites.FriendsRequest;

import java.util.ArrayList;
import java.util.List;


@Component
@NoArgsConstructor
public class FriendsRequestObjectMapperClass {


    private static ModelMapper personalDataObjectMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap <FriendsRequest, UserFriendRequestDTO>() {
            @Override
            protected void configure() {
                map().setFirstName(source.getSender().getFirstName());
                map().setLastName(source.getSender().getSurName());
                map().setUserId(source.getReceiver());
                map().setPhoto(source.getSender().getProfilePicture());
                map().setId(source.getId());
            }
        });
        return modelMapper;
    }

    //Return mapped models
    public static List <UserFriendRequestDTO> mapPersonalDataToPersonalDataDTO(List <FriendsRequest> friendsRequests) {
        List <UserFriendRequestDTO> userFriendRequestDTO = new ArrayList <>();
        friendsRequests.forEach((pd -> userFriendRequestDTO.add(personalDataObjectMapper().map(pd , UserFriendRequestDTO.class))));
        return userFriendRequestDTO;
    }

    //Return mapped model
    public static UserFriendRequestDTO mapPersonalDataToPersonalDataDTO(FriendsRequest friendsRequest) {
        UserFriendRequestDTO userFriendRequestDTO;
        userFriendRequestDTO = personalDataObjectMapper().map(friendsRequest , UserFriendRequestDTO.class);
        return userFriendRequestDTO;
    }


}
