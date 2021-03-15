package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.travel.travelapp.models.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
