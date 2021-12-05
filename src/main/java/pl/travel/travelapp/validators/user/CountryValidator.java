package pl.travel.travelapp.validators.user;

import org.springframework.stereotype.Component;
import pl.travel.travelapp.entites.Country;
import pl.travel.travelapp.exceptions.CountryNotFoundException;
import pl.travel.travelapp.repositories.CountryRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CountryValidator {

    private final CountryRepository countryRepository;

    public CountryValidator(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public void validate(Set <String> countries) {
        if ( countries == null || countries.isEmpty() ) {
            return;
        }
        Set <String> countriesFromDB = countryRepository.findAll().stream().map(Country::getCountry).collect(Collectors.toSet());
        Set <String> incorrect = new HashSet <>();
        for (String country : countries) {
            if ( !countriesFromDB.contains(country) ) {
                incorrect.add(country);
            }
        }
        if ( !incorrect.isEmpty() ) {
            throw new CountryNotFoundException(incorrect);
        }
    }

}
