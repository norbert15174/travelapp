package pl.travel.travelapp.mappers;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.travel.travelapp.DTO.PersonalDataDTO;
import pl.travel.travelapp.models.PersonalData;


import java.util.ArrayList;
import java.util.List;


//This class is responsible for mapping PersonalData to PersonalDataDTO
@Component
@NoArgsConstructor
public class PersonalDataObjectMapperClass {


    //Init model mapper PersonalData to PersonalDataDTO
    private static ModelMapper personalDataObjectMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PersonalData, PersonalDataDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setBirthday(source.getBirthDate());
                map().setFirstName(source.getFirstName());
                map().setNationality(source.getNationality());
                map().setPersonalDescription(source.getPersonalDescription());
                map().setPhoneNumber(source.getPhoneNumber());
                map().setProfilePicture(source.getProfilePicture());
                map().setSurName(source.getSurName());

            }
        });
        return modelMapper;
    }

    //Return mapped models
    public static List<PersonalDataDTO> mapPersonalDataToPersonalDataDTO(List<PersonalData> personalData) {
        List<PersonalDataDTO> personalDataDTOS = new ArrayList<>();
        personalData.forEach((pd -> personalDataDTOS.add(personalDataObjectMapper().map(pd, PersonalDataDTO.class))));
        return personalDataDTOS;
    }

    //Return mapped model
    public static PersonalDataDTO mapPersonalDataToPersonalDataDTO(PersonalData personalData) {
        PersonalDataDTO personalDataDTOS;
        personalDataDTOS = personalDataObjectMapper().map(personalData, PersonalDataDTO.class);
        return personalDataDTOS;
    }


}
