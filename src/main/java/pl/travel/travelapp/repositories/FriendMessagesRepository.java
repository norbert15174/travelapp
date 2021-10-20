package pl.travel.travelapp.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.FriendMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface FriendMessagesRepository extends JpaRepository <FriendMessages, Long> {

    @Query("select new pl.travel.travelapp.DTO.MessageDTO(fm) from FriendMessages fm" +
            " left join fm.friends f" +
            " left join f.firstUser fu" +
            " left join f.secondUser su" +
            " where f.id = :friendId and (su.id = :userId or fu.id = :userId) order by fm.date desc")
    List <MessageDTO> findMessages(@Param("userId") Long userId , @Param("friendId") Long friendId , Pageable page);


    @Query("select fm from FriendMessages fm" +
            " left join fm.sender fms " +
            " left join fm.friends f" +
            " left join f.firstUser fu" +
            " left join f.secondUser su" +
            " where f.id = :friendId and (su.id = :userId or fu.id = :userId) and fms.id <> :userId and fm.messageStatus = pl.travel.travelapp.entites.enums.MessageStatus.NEW")
    Set <FriendMessages> findUserNewMessages(Long userId , Long friendId);

    @Query("select new pl.travel.travelapp.DTO.MessageDTO(m) from FriendMessages m where m.date > :date and m.friends.id = :friendsId order by m.date descó")
    Set <MessageDTO> findMessagesAfter(LocalDateTime date , Long friendsId);
}
