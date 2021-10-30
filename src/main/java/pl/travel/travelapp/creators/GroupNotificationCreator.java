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
        GroupNotification groupNotification = create(group , user , GroupNotificationType.GROUP_REQUEST , actionUser);
        groupNotification.setGroupRequestId(request.getId());
        return groupNotification;
    }


    public static GroupNotification createRemoveUserFromGroup(UsersGroup group , PersonalData user) {
        return create(group , user , GroupNotificationType.REMOVE_USER , group.getOwner());
    }

    private static GroupNotification create(UsersGroup group , PersonalData user , GroupNotificationType type , PersonalData actionUser) {
        GroupNotification groupNotification = new GroupNotification();
        groupNotification.setActionUser(actionUser);
        groupNotification.setUser(user);
        groupNotification.setGroup(group);
        groupNotification.setStatus(NotificationGroupStatus.NEW);
        groupNotification.setType(type);
        groupNotification.setDateTime(LocalDateTime.now());
        return groupNotification;
    }

}
