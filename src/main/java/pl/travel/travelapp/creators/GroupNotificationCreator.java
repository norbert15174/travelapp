package pl.travel.travelapp.creators;

import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.GroupNotification;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.entites.enums.GroupNotificationType;
import pl.travel.travelapp.entites.enums.NotificationGroupStatus;

import java.time.LocalDateTime;

public class GroupNotificationCreator {

    public static GroupNotification createGroupRequestNotification(UsersGroup group , PersonalData user , PersonalData actionUser , GroupMemberRequest request) {
        GroupNotification groupNotification = new GroupNotification();
        groupNotification.setActionUser(actionUser);
        groupNotification.setUser(user);
        groupNotification.setGroup(group);
        groupNotification.setStatus(NotificationGroupStatus.NEW);
        groupNotification.setType(GroupNotificationType.GROUP_REQUEST);
        groupNotification.setDateTime(LocalDateTime.now());
        groupNotification.setGroupRequestId(request.getId());
        return groupNotification;
    }


}
