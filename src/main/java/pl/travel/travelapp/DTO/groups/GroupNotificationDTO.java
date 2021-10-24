package pl.travel.travelapp.DTO.groups;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.GroupNotification;
import pl.travel.travelapp.entites.enums.GroupNotificationType;
import pl.travel.travelapp.entites.enums.NotificationGroupStatus;
import pl.travel.travelapp.mappers.PersonalDataToPersonalDataInformationMapper;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupNotificationDTO {
    private PersonalInformationDTO actionUser;
    private String groupName;
    private String groupPicture;
    private Long groupId;
    private Long groupRequestId;
    private Long pictureId;
    private Long groupAlbumId;
    private GroupNotificationType type;
    private NotificationGroupStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime notificationTime;


    public GroupNotificationDTO(GroupNotification groupNotification) {
        this.actionUser = PersonalDataToPersonalDataInformationMapper.mapPersonalDataToPersonalInformationDTO(groupNotification.getActionUser());
        this.groupName = groupNotification.getGroup().getGroupName();
        this.groupPicture = groupNotification.getGroup().getGroupPicture();
        this.groupId = groupNotification.getGroup().getId();
        this.notificationTime = groupNotification.getDateTime();
        this.type = groupNotification.getType();
        this.status = groupNotification.getStatus();
        this.groupRequestId = groupNotification.getGroupRequestId();
    }

}
