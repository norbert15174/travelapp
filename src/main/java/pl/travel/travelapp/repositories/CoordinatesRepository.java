package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.Coordinates;
@Repository
public interface CoordinatesRepository extends JpaRepository <Coordinates,Long> {
}
