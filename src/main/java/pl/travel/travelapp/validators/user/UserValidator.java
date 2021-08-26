package pl.travel.travelapp.validators.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.services.query.interfaces.IUserQueryService;
import pl.travel.travelapp.validators.BaseValidator;

@Service
public class UserValidator extends BaseValidator {

    private final IUserQueryService userQueryService;
    private final CountryRepository countryRepository;

    public UserValidator(IUserQueryService userQueryService , CountryRepository countryRepository) {
        this.userQueryService = userQueryService;
        this.countryRepository = countryRepository;
    }


    public void validateUserRegisterData(UserRegisterDTO user , Errors errors) {
        validateValueNonNull("email" , user.getEmail() , errors);
        if ( validateValueNonNull("password" , user.getPassword() , errors) ) {
            if ( !validatePassword(user.getPassword()) ) {
                errors.reject("password" , "Passwords must contain at least 8 characters and include:" +
                        " Uppercase characters (A-Z) " +
                        "Lowercase characters (a-z) " +
                        "Digits (0-9)" +
                        "Special characters");
            }
        }
        validateValueNonNull("login" , user.getLogin() , errors);
        validateValueNonNull("nationality" , user.getNationality() , errors);
        validateValueNonNull("birthday" , user.getBirthDay() , errors);
        validateValueNonNull("firstName" , user.getFirstName() , errors);
        validateValueNonNull("SurName" , user.getSurName() , errors);
        validateOptional("country",countryRepository.findFirstByCountry(user.getNationality()), errors);

    }


}
