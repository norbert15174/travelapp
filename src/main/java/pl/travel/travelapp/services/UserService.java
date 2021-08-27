package pl.travel.travelapp.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import pl.travel.travelapp.DTO.UserLoginDTO;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.configuration.PasswordEncoderConfiguration;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.PersonalDescription;
import pl.travel.travelapp.entites.User;
import pl.travel.travelapp.entites.models.PasswordChangeModel;
import pl.travel.travelapp.interfaces.UserServiceInterface;
import pl.travel.travelapp.mail.google.MailService;
import pl.travel.travelapp.mail.google.html.HtmlContent;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.repositories.PersonalDataRepository;
import pl.travel.travelapp.repositories.UserRepository;
import pl.travel.travelapp.services.query.interfaces.IUserQueryService;
import pl.travel.travelapp.validators.user.UserValidator;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

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
    private final IUserQueryService userQueryService;
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository , PersonalDataRepository personalDataRepository , PasswordEncoderConfiguration passwordEncoderConfiguration , PasswordEncoder passwordEncoder , CountryRepository countryRepository , MailService mailService , IUserQueryService userQueryService , UserValidator userValidator) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.passwordEncoder = passwordEncoder;
        this.countryRepository = countryRepository;
        this.mailService = mailService;
        this.userQueryService = userQueryService;
        this.userValidator = userValidator;
    }

    private String generateJwt(String username , String password) {
        Algorithm algorithm = Algorithm.HMAC512(key);
        return JWT.create().withClaim("username" , username).withClaim("password" , password).sign(algorithm);
    }

    private String generateJwt(String username , Long PIN) {
        Algorithm algorithm = Algorithm.HMAC512(key);
        return JWT.create().withClaim("username" , username).withClaim("PIN" , String.valueOf(PIN)).sign(algorithm);
    }

    @Override
    public ResponseEntity <String> userRegister(UserRegisterDTO user) {
        Errors errors = new BeanPropertyBindingResult(user , "user");
        userValidator.validateUserRegisterData(user , errors);
        if ( errors.hasErrors() ) {
            return new ResponseEntity(errors.getAllErrors() , HttpStatus.BAD_REQUEST);
        }
        if ( userQueryService.checkIfExist(user) )
            return new ResponseEntity <>("an account with this login or e-mail address already exists" , HttpStatus.CONFLICT);
        try {
            boolean isCreated = userRegisterSave(user);
            if ( isCreated && mailService.sendMailByGoogleMailApi(user.getEmail() , "Travel App Account" , HtmlContent.readHtmlRegistration(user.getLogin() , generateJwt(user.getLogin() , user.getPassword()))) ) {
                return new ResponseEntity <>("Account has been created" , HttpStatus.OK);
            }
        } catch ( Exception e ) {
            return new ResponseEntity <>("Account hasn't been created: " + e.getMessage() , HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity <>("Account hasn't been created" , HttpStatus.NOT_IMPLEMENTED);
    }

    @Transactional
    public boolean userRegisterSave(UserRegisterDTO user) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            PersonalData userPersonalData = new PersonalData();
            User userToSave = new User();
            userToSave.setEmail(user.getEmail().toLowerCase());
            userToSave.setLogin(user.getLogin().toLowerCase());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userPersonalData.setFirstName(user.getFirstName());
            userPersonalData.setSurName(user.getSurName());
            userPersonalData.setBirthDate(LocalDate.parse(user.getBirthDay() , dateTimeFormatter));
            userPersonalData.setNationality(countryRepository.findFirstByCountry(user.getNationality()).get());
            userPersonalData.setPersonalDescription(new PersonalDescription());
            userToSave.setPersonalData(userPersonalData);
            userRepository.save(userToSave);
            return true;
        } catch ( Exception e ) {
            System.err.println("Account hasn't been created " + e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean enableUserAccount(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(key)).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            String username = verify.getClaim("username").asString();
            Optional <User> userApp = userQueryService.findFirstByLogin(username);
            if ( userApp.isEmpty() ) return false;
            userApp.get().setEnable(true);

            return userRepository.save(userApp.get()).isEnable();
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    @Transactional
    public ResponseEntity <String> changePassword(PasswordChangeModel passwords , Principal principal) {
        try {
            Optional <User> userApp = userQueryService.findFirstByLogin(principal.getName());
            if ( userApp.isPresent() ) {
                User userToSave = userApp.get();
                if ( passwordEncoder.matches(passwords.getOldPassword() , userToSave.getPassword()) ) {
                    userToSave.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                    try {
                        if ( !userValidator.validatePassword(passwords.getNewPassword()) ) {
                            return new ResponseEntity <>("Password does not pass pattern test" , HttpStatus.NOT_ACCEPTABLE);
                        }
                        userRepository.save(userToSave);
                        return new ResponseEntity <>("Password has been changed" , HttpStatus.OK);
                    } catch ( Exception e ) {
                        System.err.println(e);
                        return new ResponseEntity("Password hasn't been changed" , HttpStatus.NOT_MODIFIED);
                    }
                }
            }
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch ( UsernameNotFoundException e ) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public boolean forgetPassword(String email) {
        try {
            if ( email.isBlank() ) return false;
            Optional <User> user = userQueryService.findFirstByEmail(email);
            if ( user.isPresent() ) {
                mailService.sendMailByGoogleMailApi(email , "Travel App, new password" , HtmlContent.newPasswordHtml(user.get().getLogin() , ""));
                return true;
            }
        } catch ( NullPointerException e ) {
            return false;
        }
        return false;
    }

    @Override
    public ResponseEntity <UserLoginDTO> login(UserLoginDTO user) {
        try {
            User loggedUser = loadUserByUsernameAll(user.getLogin());
            if ( passwordEncoder.matches(user.getPassword() , loggedUser.getPassword()) ) {
                String token = generateJwt(user.getLogin() , loggedUser.getPIN());
                user.setToken(token);
                user.setRole(String.valueOf(loggedUser.getAuthorities()));
                user.setPassword(" ");
                return new ResponseEntity <>(user , HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch ( UsernameNotFoundException | NullPointerException e ) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity deleteAccountMessage(Principal user) {
        User userToDelete = userQueryService.findFirstByLogin(user.getName()).get();
        mailService.sendMailByGoogleMailApi(userToDelete.getEmail() , "Delete account, Travel App" , HtmlContent.deleteAccountTemplate(user.getName() , ""));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity <String> deleteAccount(Principal user , Map <String, String> fields) {
            String password = fields.get("password");
            if ( password.isBlank() ) return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            Optional <User> userApp = userQueryService.findFirstByLogin(user.getName());
            if ( userApp.isPresent() ) {
                User userToDelete = userApp.get();
                if ( passwordEncoder.matches(password , userToDelete.getPassword()) ) {
                    try {
                        userRepository.delete(userToDelete);
                        return new ResponseEntity <>("Account has been deleted" , HttpStatus.OK);
                    } catch ( Exception e ) {
                        System.err.println(e);
                        return new ResponseEntity("Account hasn't been deleted" , HttpStatus.NOT_MODIFIED);
                    }
                } else {
                    return new ResponseEntity <>("PASSWORD INCORRECT" , HttpStatus.NOT_ACCEPTABLE);
                }

            }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userQueryService.findByLogin(s);
    }

    public User loadUserByUsernameAll(String s) throws UsernameNotFoundException {
        return userQueryService.findByLogin(s);
    }

    public UserDetails accountVerifyToken(String username , long PIN) {
        User userDetails = loadUserByUsernameAll(username);
        if ( PIN == userDetails.getPIN() && userDetails.isEnabled() ) return loadUserByUsername(username);
        return null;
    }
}
