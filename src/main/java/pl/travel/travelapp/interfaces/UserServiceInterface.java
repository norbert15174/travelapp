package pl.travel.travelapp.interfaces;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.UserLoginDTO;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.entites.models.PasswordChangeModel;

import java.security.Principal;
import java.util.Map;

public interface UserServiceInterface {
    ResponseEntity <String> userRegister(UserRegisterDTO user);

    boolean enableUserAccount(String token);

    ResponseEntity <String> changePassword(PasswordChangeModel passwords , Principal principal);

    boolean forgetPassword(String email);

    ResponseEntity <UserLoginDTO> login(UserLoginDTO user);

    ResponseEntity deleteAccountMessage(Principal user);

    ResponseEntity <String> deleteAccount(Principal user , Map <String, String> fields);

}
