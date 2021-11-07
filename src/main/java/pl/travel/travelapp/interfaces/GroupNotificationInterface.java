package pl.travel.travelapp.interfaces;

import pl.travel.travelapp.DTO.groups.GroupNotificationDTO;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.GroupNotification;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;

import java.util.List;
import java.util.Set;

public interface GroupNotificationInterface {

    GroupNotification getGroupNotificationByUserAndGroupAndRequestId(Long userId , Long groupId , Long requestId);

    GroupNotification getGroupNotificationAcceptedByUserAndGroupAndRequestId(Long userId , Long groupId , Long requestId);

    List <GroupNotificationDTO> getUserGroupNotification(Long userId , Integer size , Integer pageNumber);

    GroupNotification create(GroupNotification groupNotification);

    GroupNotification createGroupRequest(UsersGroup group , PersonalData user , PersonalData actionUser , GroupMemberRequest request);

    void update(GroupNotification groupNotification);

    GroupNotification createRemoveUserFromGroup(UsersGroup group , PersonalData user);

    void delete(GroupNotification groupNotification);

    GroupNotification createNewAlbum(UsersGroup group , PersonalData user , Long albumId);

    GroupNotification changedAlbumOwner(UsersGroup group , PersonalData user , Long albumId , PersonalData owner);

    GroupNotification tagUser(UsersGroup group , PersonalData user , Long albumId , Long photoId , PersonalData actionUser);

    void deleteAllByUserIdAndPhotoId(Set <PersonalData> collect , Long photoId);

    void createCommentNotificationIfNeeded(UsersGroup group , PersonalData owner , Long id , Long id1, PersonalData actionUser);
}
