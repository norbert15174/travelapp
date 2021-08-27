package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.entites.Friends;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository <Friends, Long> {
    @Query("select distinct f from Friends f left join fetch f.firstUser left join fetch f.secondUser where f.firstUser.id = :id or f.secondUser.id = :id")
    List <Friends> findFriends(long id);

    @Query("select f from Friends f left join fetch f.firstUser left join fetch f.secondUser where f.id = :id")
    Optional <Friends> findFriendById(long id);

    @Query("select f from Friends f " +
            "left join fetch f.firstUser " +
            "left join fetch f.secondUser " +
            "where (f.firstUser.id = :first and f.secondUser.id = :second) or (f.firstUser.id = :second and f.secondUser.id = :first)")
    Optional<Friends> findFriendByFirstAndSecond(long first, long second);
}
