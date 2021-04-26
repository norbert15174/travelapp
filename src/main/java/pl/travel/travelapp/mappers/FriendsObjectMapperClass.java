package pl.travel.travelapp.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import pl.travel.travelapp.DTO.FriendsDTO;
import pl.travel.travelapp.models.PersonalData;

import java.util.ArrayList;
import java.util.List;

public class FriendsObjectMapperClass {
    private static ModelMapper personalDataObjectMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap <PersonalData, FriendsDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setLastName(source.getSurName());
                map().setName(source.getFirstName());
                map().setProfilePicture(source.getProfilePicture());
            }
        });
        return modelMapper;
    }


    public static List <FriendsDTO> mapPersonalDataToFriendsDTOO(List<PersonalData> person) {
        List<FriendsDTO> friendsDTOS = new ArrayList <>();
        person.forEach((pd -> friendsDTOS.add(personalDataObjectMapper().map(pd, FriendsDTO.class))));
        return friendsDTOS;
    }

    public static FriendsDTO mapPersonalDataToFriendsDTO(PersonalData person) {
        FriendsDTO friendsDTOS;
        friendsDTOS = personalDataObjectMapper().map(person, FriendsDTO.class);
        return friendsDTOS;
    }
}
