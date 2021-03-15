package pl.travel.travelapp.services;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.configuration.PasswordEncoderConfiguration;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.UserRepository;

public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PersonalDataRepository personalDataRepository;
    private PasswordEncoderConfiguration passwordEncoderConfiguration;
    @Autowired
    public UserService(UserRepository userRepository, PersonalDataRepository personalDataRepository, PasswordEncoderConfiguration passwordEncoderConfiguration) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }


    public ResponseEntity<String> UserRegister(UserRegisterDTO user){
        if(userRepository.checkIfExist(user.getLogin(),user.getEmail()).isPresent()) return new ResponseEntity<>("an account with this login or e-mail address already exists",HttpStatus.CONFLICT);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @Transactional
//    public boolean UserRegisterSave(UserRegisterDTO user){
//
//    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByLogin(s);
    }
}
