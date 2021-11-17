package pl.travel.travelapp.creators;

import pl.travel.travelapp.entites.*;
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

    public static GroupNotification createNewAlbum(UsersGroup group , PersonalData user , PersonalData actionUser , Long albumId) {
        GroupNotification groupNotification = create(group , user , GroupNotificationType.NEW_ALBUM , actionUser);
        groupNotification.setGroupAlbumId(albumId);
        return groupNotification;
    }

    public static GroupNotification changedAlbumOwner(UsersGroup group , PersonalData user , Long albumId , PersonalData groupOwner) {
        GroupNotification groupNotification = create(group , groupOwner , GroupNotificationType.CHANGE_ALBUM_OWNER , user);
        groupNotification.setGroupAlbumId(albumId);
        return groupNotification;
    }

    public static GroupNotification tagUser(UsersGroup group , PersonalData user , Long albumId , Long photoId , PersonalData actionUser) {
        GroupNotification groupNotification = create(group , user , GroupNotificationType.PHOTO_MARKED , actionUser);
        groupNotification.setGroupAlbumId(albumId);
        groupNotification.setGroupPhotoId(photoId);
        return groupNotification;
    }

    public static GroupNotification createCommentNotification(UsersGroup group , PersonalData owner , Long groupAlbumId , Long photoId , PersonalData actionUser) {
        GroupNotification groupNotification = create(group , owner , GroupNotificationType.PHOTO_COMMENT , actionUser);
        groupNotification.setGroupAlbumId(groupAlbumId);
        groupNotification.setGroupPhotoId(photoId);
        return groupNotification;
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

    public static GroupNotification deleteAlbum(UsersGroup group , PersonalData userNotification , GroupAlbum groupAlbum , PersonalData user) {
        GroupNotification groupNotification = create(group , userNotification , GroupNotificationType.DELETE_ALBUM , user);
        groupNotification.setAlbumName(groupAlbum.getName());
        return groupNotification;
    }
}
