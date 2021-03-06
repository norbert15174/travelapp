package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.travel.travelapp.entites.TaggedUser;

import java.util.Set;

public interface TaggedPhotoRepository extends JpaRepository <TaggedUser, Long> {

    @Query("SELECT t FROM TaggedUser t WHERE t.userId = :id")
    Set <TaggedUser> findUserTagByUserId(Long id);

}
