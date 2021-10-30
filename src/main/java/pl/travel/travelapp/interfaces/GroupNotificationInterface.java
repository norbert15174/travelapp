package pl.travel.travelapp.interfaces;

import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupNotificationDTO;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.GroupNotification;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;

import java.util.List;

public interface GroupNotificationInterface {

    GroupNotification getGroupNotificationByUserAndGroupAndRequestId(Long userId , Long groupId , Long requestId);

    List <GroupNotificationDTO> getUserGroupNotification(Long userId , Integer size , Integer pageNumber);

    GroupNotification create(GroupNotification groupNotification);

    GroupNotification createGroupRequest(UsersGroup group , PersonalData user , PersonalData actionUser , GroupMemberRequest request);

    void update(GroupNotification groupNotification);

    GroupNotification createRemoveUserFromGroup(UsersGroup group , PersonalData user);

    void delete(GroupNotification groupNotification);
}
