package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import pl.travel.travelapp.DTO.UserLoginDTO;
import pl.travel.travelapp.DTO.UserRegisterDTO;

import java.util.Map;

public interface UserServiceInterface {
    ResponseEntity<String> userRegister(UserRegisterDTO user);

    boolean enableUserAccount(String token);

    boolean userRegisterSave(UserRegisterDTO user) throws Exception;
    ResponseEntity<String> changePassword(Map<String, String> fields);

    boolean forgetPassword(String email);

   ResponseEntity<UserLoginDTO> login(UserLoginDTO user);

    ResponseEntity deleteAccountMessage(UserDetails user);
    ResponseEntity<String> deleteAccount(UserDetails user, String password);

}
