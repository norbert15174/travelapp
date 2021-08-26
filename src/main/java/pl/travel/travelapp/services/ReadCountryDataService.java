package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.models.Country;
import pl.travel.travelapp.repositories.CountryRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class ReadCountryDataService {

    private CountryRepository countryRepository;

    @Autowired
    public ReadCountryDataService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    //@EventListener(ApplicationReadyEvent.class)
    public List <Country> readFileMethodPerson() {
        List <Country> countries = new ArrayList <>();
        File file = new File("src\\main\\resources\\CountriesAndFlags\\Country_Flags.csv");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                countries.add(splitDataCountries(scanner.nextLine()));
            }
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }

        return countryRepository.saveAll(countries);

    }

    private Country splitDataCountries(String nextLine) {
        String[] data = nextLine.split(",");
        return new Country(data[0] , data[2]);
    }

}
