package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import pl.travel.travelapp.DTO.UserLoginDTO;
import pl.travel.travelapp.DTO.UserRegisterDTO;

import java.security.Principal;
import java.util.Map;

public interface UserServiceInterface {
    ResponseEntity<String> userRegister(UserRegisterDTO user);

    boolean enableUserAccount(String token);

    ResponseEntity<String> changePassword(Map<String, String> fields);

    boolean forgetPassword(String email);

   ResponseEntity<UserLoginDTO> login(UserLoginDTO user);

    ResponseEntity deleteAccountMessage(Principal user);
    ResponseEntity<String> deleteAccount(Principal user,Map<String, String> fields);

}
