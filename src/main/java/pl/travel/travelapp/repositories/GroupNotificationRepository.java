package pl.travel.travelapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.travel.travelapp.entites.GroupNotification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface GroupNotificationRepository extends JpaRepository <GroupNotification, Long> {

    @Query("select gn from GroupNotification gn " +
            "inner join fetch gn.group g " +
            "inner join fetch gn.actionUser " +
            "inner join fetch gn.user u where u.id = :userId order by gn.dateTime desc")
    List <GroupNotification> findPageByUserId(@Param("userId") Long userId , Pageable page);

    @Query("select gn from GroupNotification gn " +
            "inner join fetch gn.group g " +
            "inner join fetch gn.actionUser " +
            "inner join fetch gn.user u " +
            "where u.id = :userId and g.id = :groupId and gn.groupRequestId = :requestId and gn.status <> pl.travel.travelapp.entites.enums.NotificationGroupStatus.ACCEPTED")
    GroupNotification findGroupNotificationByUserAndGroupAndRequestId(@Param("userId") Long userId , @Param("groupId") Long groupId , @Param("requestId") Long requestId);

    @Query("select gn from GroupNotification gn " +
            "inner join fetch gn.group g " +
            "inner join fetch gn.actionUser " +
            "inner join fetch gn.user u " +
            "where u.id = :userId and g.id = :groupId and gn.groupRequestId = :requestId and gn.status = pl.travel.travelapp.entites.enums.NotificationGroupStatus.ACCEPTED")
    GroupNotification findGroupNotificationAcceptedByUserAndGroupAndRequestId(@Param("userId") Long userId , @Param("groupId") Long groupId , @Param("requestId") Long requestId);

    @Modifying
    @Query("delete from GroupNotification gn where (gn.groupPhotoId = :photoId and gn.user.id in :userIds and gn.type = pl.travel.travelapp.entites.enums.GroupNotificationType.PHOTO_MARKED)")
    void deleteGroupNotificationByUserIdsAndPhoto(Set <Long> userIds , Long photoId);

    @Query("select gn from GroupNotification gn " +
            "inner join fetch gn.group g " +
            "inner join fetch gn.actionUser " +
            "inner join fetch gn.user u " +
            "where u.id = :userId and gn.groupPhotoId = :photoId and " +
            "gn.type = pl.travel.travelapp.entites.enums.GroupNotificationType.PHOTO_COMMENT and " +
            "gn.dateTime > :date order by gn.dateTime desc ")
    List <GroupNotification> findByUserIdAndPhotoIdAfterDate(Long userId , Long photoId , LocalDateTime date);
}
