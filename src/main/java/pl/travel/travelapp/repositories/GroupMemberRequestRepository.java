package pl.travel.travelapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.entites.GroupMemberRequest;

import java.util.Optional;
import java.util.Set;

public interface GroupMemberRequestRepository extends JpaRepository <GroupMemberRequest, Long> {
    @Query("select gmr from GroupMemberRequest gmr inner join gmr.user inner join gmr.group where gmr.id = :requestId and gmr.isMember = false")
    Optional <GroupMemberRequest> findRequestById(@Param("requestId") Long requestId);

    @Query("select new pl.travel.travelapp.DTO.groups.GroupRequestDTO(gmr) from GroupMemberRequest gmr inner join gmr.user u inner join gmr.group where u.id = :userId and gmr.isMember = false order by gmr.dateTime desc")
    Set <GroupRequestDTO> findUserGroupRequests(@Param("userId") Long userId);

    @Query("select gmr from GroupMemberRequest gmr inner join gmr.user u inner join gmr.group where u.id = :userId and gmr.isMember = false order by gmr.dateTime desc")
    Set <GroupMemberRequest> findUserGroupMemberRequest(@Param("userId") Long id);
}
