package pl.travel.travelapp.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;

import java.util.List;

@Repository
public interface FriendMessagesRepository extends JpaRepository <FriendMessages, Long> {

    @Query("select new pl.travel.travelapp.DTO.MessageDTO(fm) from FriendMessages fm" +
            " left join fm.friends f" +
            " left join f.firstUser fu" +
            " left join f.secondUser su" +
            " where f.id = :friendId and (su.id = :userId or fu.id = :userId) order by fm.date desc")
    List <MessageDTO> findMessages(@Param("userId") Long userId , @Param("friendId") Long friendId , Pageable page);

}
