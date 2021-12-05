package pl.travel.travelapp.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.travel.travelapp.entites.IndividualAlbum;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.specification.criteria.AlbumSearchCriteria;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;

import static org.springframework.data.jpa.domain.Specification.where;

public class AlbumSpecification extends BaseSpecification <IndividualAlbum, AlbumSearchCriteria> {

    @Override
    public Specification getFilter(AlbumSearchCriteria request) {
        return where((nameAndSurname(request.getOwnerName())
                .or(surnameAndName(request.getOwnerName())))
                .and(place(request.getPlace()))
                .and(name(request.getName()))
                .and(access(request.getUserId())));
    }

    private Specification <PersonalData> nameAndSurname(String name) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(name) ) {
                return null;
            }
            Expression <String> firstName = criteriaBuilder.concat(root.join("owner").get("firstName") , " ");
            Expression <String> expression = criteriaBuilder.concat(firstName , root.join("owner").get("surName"));
            return criteriaBuilder.like(expression , containsLoweCaseAndStartAt(name));
        };
    }

    private Specification <PersonalData> surnameAndName(String name) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(name) ) {
                return null;
            }
            Expression <String> surname = criteriaBuilder.concat(root.join("owner").get("surName") , " ");
            Expression <String> expression = criteriaBuilder.concat(surname , root.join("owner").get("firstName"));
            return criteriaBuilder.like(expression , containsLoweCaseAndStartAt(name));
        };
    }

    private Specification <PersonalData> country(String country) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(country) ) {
                return null;
            }
            return criteriaBuilder.like(root.join("coordinate").join("country").get("country") , country);
        };
    }

    private Specification <PersonalData> place(String place) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(place) ) {
                return null;
            }
            return criteriaBuilder.like(root.join("coordinate").get("place") , containsLoweCaseAndStartAt(place));
        };
    }

    private Specification <PersonalData> name(String name) {
        return (root , query , criteriaBuilder) -> {
            if ( isNullOrEmptyString(name) ) {
                return null;
            }
            return criteriaBuilder.like(root.get("name") , containsLoweCaseAndStartAt(name));
        };
    }


    private Specification <PersonalData> access(Long userId) {
        return (root , query , criteriaBuilder) -> {
            return criteriaBuilder.or(root.get("isPublic").in(true) , root.join("sharedAlbum" , JoinType.LEFT).get("userId").in(userId));
        };
    }

}
