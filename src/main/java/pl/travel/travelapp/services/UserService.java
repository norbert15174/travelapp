package pl.travel.travelapp.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.configuration.PasswordEncoderConfiguration;
import pl.travel.travelapp.mail.google.MailService;
import pl.travel.travelapp.models.Country;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.User;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.UserRepository;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

    private final static String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    private UserRepository userRepository;
    private PersonalDataRepository personalDataRepository;
    private PasswordEncoderConfiguration passwordEncoderConfiguration;
    private PasswordEncoder passwordEncoder;
    private CountryRepository countryRepository;
    private MailService mailService;
    @Autowired
    public UserService(UserRepository userRepository, PersonalDataRepository personalDataRepository, PasswordEncoderConfiguration passwordEncoderConfiguration, PasswordEncoder passwordEncoder, CountryRepository countryRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.passwordEncoder = passwordEncoder;
        this.countryRepository = countryRepository;
        this.mailService = mailService;
    }
    //Checking the data and returning the registration result
    public ResponseEntity<String> userRegister(UserRegisterDTO user){
        if(userRepository.checkIfExist(user.getLogin(),user.getEmail()).isPresent()) return new ResponseEntity<>("an account with this login or e-mail address already exists",HttpStatus.CONFLICT);
        if(!isValidPassword(user.getPassword(),regex)) return new ResponseEntity<>("Your password doesn't suit requirements",HttpStatus.NOT_ACCEPTABLE);
        if(!countryRepository.findFirstByCountry(user.getNationality()).isPresent()) return new ResponseEntity<>("Country doesn't exist",HttpStatus.NOT_FOUND);
        try {
            boolean isCreated = userRegisterSave(user);
            if(isCreated && mailService.sendMailByGoogleMailApi(user.getEmail(),"Travel App Account","<h1>Thank you for creating an account</h1>"))
            {
                return new ResponseEntity<>("Account has been created",HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Account hasn't been created",HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>("Undefined error",HttpStatus.NOT_FOUND);
    }
    //Saving new user to database
    @Transactional
    public boolean userRegisterSave(UserRegisterDTO user) throws Exception{
        try {
            PersonalData userPersonalData = new PersonalData();
            User userToSave = new User();
            userToSave.setEmail(user.getEmail());
            userToSave.setLogin(user.getLogin());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userPersonalData.setFirstName(user.getFirstName());
            userPersonalData.setSurName(user.getSurName());
            userPersonalData.setBirthDate(user.getBirthDay());
            userPersonalData.setNationality(countryRepository.findFirstByCountry(user.getNationality()).get());
            userRepository.save(userToSave);
            personalDataRepository.save(userPersonalData);
            return true;
        }catch (Exception e){
            System.err.println("Account hasn't been created " + e);
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByLogin(s);
    }
    //Checking if the password meets the requirements
    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

//          Class to test registration process
//    @EventListener(ApplicationReadyEvent.class)
//    public void testRegister(){
//        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
//        userRegisterDTO.setEmail("faronnorbertkrk@gmail.com");
//        userRegisterDTO.setBirthDay(LocalDate.now());
//        userRegisterDTO.setFirstName("Norbert");
//        userRegisterDTO.setLogin("norbi1234");
//        userRegisterDTO.setSurName("Faron");
//        userRegisterDTO.setPassword("N@jwalxcm123ka");
//        userRegisterDTO.setNationality("Poland");
//        userRegister(userRegisterDTO);
//    }

}
