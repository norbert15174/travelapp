package pl.travel.travelapp.validators;

import org.springframework.validation.Errors;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseValidator<T> {

    private final static String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";

    public void validateObjectNonNull(String key , Object object , Errors errors) {
        if ( object == null ) {
            errors.rejectValue(key , "Object can not be null");
        }
    }

    public boolean validateValueNonNull(String key , String value , Errors errors) {
        if ( value == null || value.isBlank() ) {
            errors.reject(key , "Value can not be blank");
            return false;
        }
        return true;
    }

    public boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean validateOptional(String key , Optional <T> objectOpt , Errors errors) {
        if ( objectOpt.isEmpty() ) {
            errors.reject(key , "Object is empty");
            return false;
        }
        return true;
    }

}
