package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.travel.travelapp.DTO.UserLoginDTO;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.services.UserService;

import java.security.Principal;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity <String> userRegister(@RequestBody UserRegisterDTO user) {
        return userService.userRegister(user);
    }

    @GetMapping("/register")
    public RedirectView setUserEnable(@RequestParam("token") String token) {
        if ( userService.enableUserAccount(token) ) {
            return new RedirectView("https://www.baeldung.com/spring-redirect-and-forward");
        }
        return new RedirectView("https://www.facebook.com");
    }

    @GetMapping("/password")
    public ResponseEntity forgetPassword(@RequestParam String email) {
        if ( userService.forgetPassword(email) ) return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/password")
    public ResponseEntity changePassword(@RequestBody Map <String, String> fields) {
        return userService.changePassword(fields);
    }

    @PostMapping("/login")
    public ResponseEntity <UserLoginDTO> login(@RequestBody UserLoginDTO user) {
        return userService.login(user);
    }

    @GetMapping("/delete")
    public ResponseEntity deleteAccountSendMessage(Principal user) {
        return userService.deleteAccountMessage(user);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAccountSendMessage(@RequestBody Map <String, String> fields , Principal user) {
        return userService.deleteAccount(user , fields);
    }

}
