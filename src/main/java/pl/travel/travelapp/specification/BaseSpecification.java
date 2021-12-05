package pl.travel.travelapp.specification;

import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public abstract class BaseSpecification<T, U> {

    private static final String WILDCARD = "%";

    public abstract Specification <T> getFilter(U request);

    protected String containsLoweCase(String searchField) {
        return WILDCARD + searchField.toLowerCase() + WILDCARD;
    }

    protected String containsLoweCaseAndStartAt(String searchField) {
        return searchField.toLowerCase() + WILDCARD;
    }

    protected boolean isCollectionNullOrEmpty(Collection <?> collection) {
        return collection == null || collection.isEmpty();
    }

    protected boolean isNull(Object object) {
        return object == null;
    }

    protected boolean isNullOrEmptyString(String value){
        return Strings.isNullOrEmpty(value);
    }

    protected Specification <T> orderByDesc(String value) {
        return ((root , query , criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get(value)));
            return null;
        });
    }

    protected Specification <T> orderByAsc(String value) {
        return ((root , query , criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get(value)));
            return null;
        });
    }


}
