package pl.travel.travelapp.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import pl.travel.travelapp.DTO.UserLoginDTO;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.configuration.PasswordEncoderConfiguration;
import pl.travel.travelapp.interfaces.UserServiceInterface;
import pl.travel.travelapp.mail.google.MailService;
import pl.travel.travelapp.mail.google.html.HtmlContent;
import pl.travel.travelapp.models.Country;
import pl.travel.travelapp.models.PersonalData;
import pl.travel.travelapp.models.PersonalDescription;
import pl.travel.travelapp.models.User;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// !!!!!!!!!    REMEMBER TO ADD TOKEN TO METHOD(CHANGE PASSWORD, ENABLE ACCOUNT) !!!!!!!!!!!!!!!!!!!


@Service
public class UserService implements UserDetailsService, UserServiceInterface {

    private final static String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    @Value("${Algorithm-key}")
    private String key;
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

    private String generateJwt(String username, String password) {
        Algorithm algorithm = Algorithm.HMAC512(key);
        return JWT.create().withClaim("username", username).withClaim("password", password).sign(algorithm);
    }


    //Checking the data and returning the registration result
    @Override
    public ResponseEntity<String> userRegister(UserRegisterDTO user) {
        try {

            if(checkIfDataIsNotBlank(user)){
                if (userRepository.checkIfExist(user.getLogin(), user.getEmail()).isPresent())
                    return new ResponseEntity<>("an account with this login or e-mail address already exists", HttpStatus.CONFLICT);
                if (!isValidPassword(user.getPassword(), regex))
                    return new ResponseEntity<>("Your password doesn't suit requirements", HttpStatus.NOT_ACCEPTABLE);
                if ( countryRepository.findFirstByCountry(user.getNationality()).isEmpty() )
                    return new ResponseEntity<>("Country doesn't exist", HttpStatus.NOT_FOUND);
                try {
                    boolean isCreated = userRegisterSave(user);
                    if (isCreated && mailService.sendMailByGoogleMailApi(user.getEmail(), "Travel App Account", HtmlContent.readHtmlRegistration(user.getLogin(), generateJwt(user.getLogin(),user.getPassword())))) {
                        return new ResponseEntity<>("Account has been created", HttpStatus.OK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>("Account hasn't been created", HttpStatus.NOT_IMPLEMENTED);
                }

            }
        }catch (NullPointerException e){
            return new ResponseEntity<>("Send blank data is forbidden", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Undefined error", HttpStatus.NOT_FOUND);
    }


    //Checking whether user data is not blank, if yes the method throws NullPointerException
    private boolean checkIfDataIsNotBlank(UserRegisterDTO user) throws NullPointerException{
        if(user.getEmail().isBlank()) throw new NullPointerException();
        if(user.getPassword().isBlank()) throw new NullPointerException();
        if(user.getLogin().isBlank()) throw new NullPointerException();
        if(user.getNationality().isBlank()) throw new NullPointerException();
        if(user.getBirthDay().isBlank()) throw new NullPointerException();
        if(user.getFirstName().isBlank()) throw new NullPointerException();
        if(user.getSurName().isBlank()) throw new NullPointerException();
        return true;
    }



    @Transactional
    public boolean userRegisterSave(UserRegisterDTO user){
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM uuuu");
            PersonalData userPersonalData = new PersonalData();
            User userToSave = new User();
            userToSave.setEmail(user.getEmail().toLowerCase());
            userToSave.setLogin(user.getLogin().toLowerCase());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userPersonalData.setFirstName(user.getFirstName());
            userPersonalData.setSurName(user.getSurName());
            userPersonalData.setBirthDate(LocalDate.parse(user.getBirthDay(),dateTimeFormatter));
            userPersonalData.setNationality(countryRepository.findFirstByCountry(user.getNationality()).get());
            userPersonalData.setPersonalDescription(new PersonalDescription());
            userToSave.setPersonalData(userPersonalData);
            userRepository.save(userToSave);
            return true;
        } catch (Exception e) {
            System.err.println("Account hasn't been created " + e);
            return false;
        }
    }

    //Set user account enable
    @Override
    @Transactional
    public boolean enableUserAccount(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(key)).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            String username = verify.getClaim("username").asString();
            Optional<User> userApp = userRepository.findFirstByLogin(username);
            if (userApp.isEmpty()) return false;
                userApp.get().setEnable(true);

            return userRepository.save(userApp.get()).isEnable();
        }catch (Exception e){
            return false;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> changePassword(Map<String, String> fields) {

        try {
            String password = fields.get("password");
            String token = fields.get("token");


            try {
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(key)).build();
                DecodedJWT verify = jwtVerifier.verify(token);
                String login = verify.getClaim("username").asString();
                Optional<User> userApp = userRepository.findFirstByLogin(login);
                if (userApp.isPresent()) {
                    User userToSave = userApp.get();
                    userToSave.setPassword(passwordEncoder.encode(password));
                    try {
                        userRepository.save(userToSave);
                        return new ResponseEntity<>("Password has been changed", HttpStatus.OK);
                    } catch (Exception e) {
                        System.err.println(e);
                        return new ResponseEntity("Password hasn't been changed", HttpStatus.NOT_MODIFIED);
                    }
                }
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } catch (UsernameNotFoundException e) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }catch (NullPointerException e){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }


    //Send mail which allow to change password
    @Override
    public boolean forgetPassword(String email) {
        try {
            if(email.isBlank()) return false;
            Optional<User> user = userRepository.findFirstByEmail(email);
            if (user.isPresent()) {
                mailService.sendMailByGoogleMailApi(email, "Travel App, new password", HtmlContent.newPasswordHtml(user.get().getLogin(), ""));
                return true;
            }
        }catch (NullPointerException e){
            return false;
        }
        return false;
    }

    //User authorization, method return token and user information
    @Override
    public ResponseEntity<UserLoginDTO> login(UserLoginDTO user) {
        try {
            UserDetails loggedUser = loadUserByUsername(user.getLogin());
            if (passwordEncoder.matches(user.getPassword(), loggedUser.getPassword())) {
                String token = generateJwt(user.getLogin(), user.getPassword());
                user.setToken(token);
                user.setRole(String.valueOf(loggedUser.getAuthorities()));
                user.setPassword(" ");
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (UsernameNotFoundException | NullPointerException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public ResponseEntity deleteAccountMessage(Principal user) {
        User userToDelete = userRepository.findFirstByLogin(user.getName()).get();
        mailService.sendMailByGoogleMailApi(userToDelete.getEmail(), "Delete account, Travel App", HtmlContent.deleteAccountTemplate(user.getName(), ""));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAccount(Principal user,Map<String, String> fields) {
        try {
            String password = fields.get("password");
            if(password.isBlank()) throw new NullPointerException();
            Optional<User> userApp = userRepository.findFirstByLogin(user.getName());
            if (userApp.isPresent()) {
                User userToDelete = userApp.get();
                if (passwordEncoder.matches(password,userToDelete.getPassword())) {
                    try {
                        userRepository.delete(userToDelete);
                        return new ResponseEntity<>("Account has been deleted", HttpStatus.OK);
                    } catch (Exception e) {
                        System.err.println(e);
                        return new ResponseEntity("Account hasn't been deleted", HttpStatus.NOT_MODIFIED);
                    }
                } else {
                    return new ResponseEntity<>("PASSWORD INCORRECT", HttpStatus.NOT_ACCEPTABLE);
                }

            }
        }catch (NullPointerException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByLogin(s);
    }

    //Checking if the password meets the requirements
    private static boolean isValidPassword(String password, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public UserDetails accountVerify(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword()) && userDetails.isEnabled()) return userDetails;
        return null;
    }
}
