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
import pl.travel.travelapp.models.User;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService implements UserDetailsService {

    private final static String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    private UserRepository userRepository;
    private PersonalDataRepository personalDataRepository;
    private PasswordEncoderConfiguration passwordEncoderConfiguration;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PersonalDataRepository personalDataRepository, PasswordEncoderConfiguration passwordEncoderConfiguration, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<String> UserRegister(UserRegisterDTO user){
        if(userRepository.checkIfExist(user.getLogin(),user.getEmail()).isPresent()) return new ResponseEntity<>("an account with this login or e-mail address already exists",HttpStatus.CONFLICT);
        if(!isValidPassword(user.getPassword(),regex)) return new ResponseEntity<>("Your password doesn't suit requirements",HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public boolean UserRegisterSave(UserRegisterDTO user) throws Exception{
        PersonalData userPersonalData = new PersonalData();
        User userToSave = new User();
        userToSave.setEmail(user.getEmail());
        userToSave.setLogin(user.getLogin());
        userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userPersonalData.setFirstName(user.getFirstName());
        userPersonalData.setSurName(user.getSurName());
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByLogin(s);
    }

    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
