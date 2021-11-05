package pl.travel.travelapp.validators;

import org.springframework.http.ResponseEntity;
import pl.travel.travelapp.DTO.groups.GroupCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestGetDTO;
import pl.travel.travelapp.entites.ValidationErrors;

import java.security.Principal;
import java.util.Set;

public abstract class UsersGroupValidator extends BaseValidator {

    protected ValidationErrors validateCreate(GroupCreateDTO model , ValidationErrors errors) {
        if ( !validateExist(model.getDescription()) ) {
            errors.addError("description" , ": pole nie może być puste");
        }
        if ( !validateExist(model.getGroupName()) ) {
            errors.addError("groupName" , " : pole nie może być puste");
        }
        return errors;
    }
}
