package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.PersonalDataDTO;

public interface PersonalDataInterface {
    ResponseEntity<PersonalDataDTO> setPersonalDataProfilePicture(MultipartFile file, UserDetails user);
    ResponseEntity<PersonalDataDTO> setPersonalDataBackgroundPicture(MultipartFile file, UserDetails user);
    ResponseEntity<PersonalDataDTO> updatePersonalInformation(UserDetails user, PersonalDataDTO userUpdate);
    ResponseEntity<PersonalDataDTO> getUserProfile(UserDetails user);
}
