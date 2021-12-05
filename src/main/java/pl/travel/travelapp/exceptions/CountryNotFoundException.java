package pl.travel.travelapp.exceptions;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CountryNotFoundException extends RuntimeException {

    private Set <String> countries = new HashSet <>();

    public CountryNotFoundException() {
    }

    public CountryNotFoundException(Set <String> countries) {
        this.countries.addAll(countries);
    }

    @Override
    public String getMessage() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "Country: " + this.countries.stream().collect(Collectors.joining(",")) + " does not exist in portal";
    }

}
