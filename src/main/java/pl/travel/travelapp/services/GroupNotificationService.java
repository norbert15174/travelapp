package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupNotificationDTO;
import pl.travel.travelapp.creators.GroupNotificationCreator;
import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.GroupNotification;
import pl.travel.travelapp.entites.PersonalData;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.entites.enums.NotificationGroupStatus;
import pl.travel.travelapp.interfaces.GroupNotificationInterface;
import pl.travel.travelapp.repositories.GroupNotificationRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupNotificationService implements GroupNotificationInterface {

    private final GroupNotificationRepository groupNotificationRepository;

    @Autowired
    public GroupNotificationService(GroupNotificationRepository groupNotificationRepository) {
        this.groupNotificationRepository = groupNotificationRepository;
    }

    @Transactional
    @Override
    public GroupNotification getGroupNotificationByUserAndGroupAndRequestId(Long userId , Long groupId , Long requestId) {
        return groupNotificationRepository.findGroupNotificationByUserAndGroupAndRequestId(userId , groupId , requestId);
    }

    @Transactional
    @Override
    public List <GroupNotificationDTO> getUserGroupNotification(Long userId , Integer size , Integer pageNumber) {
        List <GroupNotification> groupNotifications = groupNotificationRepository.findPageByUserId(userId , PageRequest.of(size , pageNumber));
        List<GroupNotificationDTO> notifications = groupNotifications.stream().map(GroupNotificationDTO::new).collect(Collectors.toList());
        if ( !groupNotifications.isEmpty() ) {
            Set <GroupNotification> changeStatus = groupNotifications
                    .stream()
                    .filter(groupNotification -> groupNotification.getStatus().equals(NotificationGroupStatus.NEW))
                    .collect(Collectors.toSet());
            for (GroupNotification notification : changeStatus) {
                notification.setStatus(NotificationGroupStatus.SEEN);
                groupNotificationRepository.save(notification);
            }
        }
        return notifications;
    }

    @Transactional
    @Override
    public GroupNotification create(GroupNotification groupNotification) {
        return groupNotificationRepository.save(groupNotification);
    }

    @Transactional
    @Override
    public GroupNotification createGroupRequest(UsersGroup group , PersonalData user , PersonalData actionUser , GroupMemberRequest request) {
        return create(GroupNotificationCreator.createGroupRequestNotification(group , user , actionUser , request));
    }

    @Transactional
    @Override
    public void update(GroupNotification groupNotification) {
        groupNotificationRepository.save(groupNotification);
    }


}