package pl.travel.travelapp.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.specification.criteria.UserSearchCriteria;

import javax.persistence.criteria.Expression;

import static org.springframework.data.jpa.domain.Specification.where;

public class UserSpecification extends BaseSpecification <PersonalData, UserSearchCriteria> {

    @Override
    public Specification getFilter(UserSearchCriteria request) {
        return where(nameAndSurname(request.getName()).or(surnameAndName(request.getName())).and(from(request.getFrom())));
    }

    private Specification <PersonalData> nameAndSurname(String name) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(name) ) {
                return null;
            }
            Expression <String> firstName = criteriaBuilder.concat(root.get("firstName") , " ");
            Expression <String> expression = criteriaBuilder.concat(firstName , root.get("surName"));
            return criteriaBuilder.like(expression , containsLoweCaseAndStartAt(name));
        };
    }

    private Specification <PersonalData> surnameAndName(String name) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(name) ) {
                return null;
            }
            Expression <String> surname = criteriaBuilder.concat(root.get("surName") , " ");
            Expression <String> expression = criteriaBuilder.concat(surname , root.get("firstName"));
            return criteriaBuilder.like(expression , containsLoweCaseAndStartAt(name));
        };
    }

    private Specification <PersonalData> from(String country) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(country) ) {
                return null;
            }
            return criteriaBuilder.like(root.join("Nationality").get("country") , country);
        };
    }

}
