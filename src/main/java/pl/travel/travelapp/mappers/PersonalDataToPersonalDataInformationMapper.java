package pl.travel.travelapp.mappers;


import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.PersonalData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


//This class is responsible for mapping PersonalData to PersonalDataDTO
@Component
@NoArgsConstructor
public class PersonalDataToPersonalDataInformationMapper {


    private static ModelMapper personalDataToPersonalDataInformationMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap <PersonalData, PersonalInformationDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setPhoto(source.getProfilePicture());
                map().setSurName(source.getSurName());
                map().setName(source.getFirstName());
            }
        });
        return modelMapper;
    }


    public static Set <PersonalInformationDTO> mapPersonalDataToPersonalInformationDTO(Set <PersonalData> person) {
        Set <PersonalInformationDTO> friendsDTOS = new HashSet <>();
        person.forEach((pd -> friendsDTOS.add(personalDataToPersonalDataInformationMapper().map(pd , PersonalInformationDTO.class))));
        return friendsDTOS;
    }

    public static PersonalInformationDTO mapPersonalDataToPersonalInformationDTO(PersonalData person) {
        PersonalInformationDTO friendsDTOS;
        friendsDTOS = personalDataToPersonalDataInformationMapper().map(person , PersonalInformationDTO.class);
        return friendsDTOS;
    }


}
