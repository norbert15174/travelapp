package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.enums.GroupNotificationType;
import pl.travel.travelapp.entites.enums.NotificationGroupStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData user;
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersGroup group;
    @OneToOne
    private PersonalData actionUser;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    private GroupNotificationType type;
    private NotificationGroupStatus status;
    private Long groupRequestId;
    private Long groupAlbumId;
    private Long groupPhotoId;

    public boolean isNotificationSeen() {
        return status.equals(NotificationGroupStatus.SEEN);
    }

    public boolean isNotificationNew() {
        return status.equals(NotificationGroupStatus.NEW);
    }

    public boolean isGroupRequest() {
        return type.equals(GroupNotificationType.GROUP_REQUEST);
    }

    public boolean isAlbumDelete() {
        return type.equals(GroupNotificationType.DELETE_ALBUM);
    }

    public boolean isAlbumCreate() {
        return type.equals(GroupNotificationType.NEW_ALBUM);
    }

    public boolean isPhotoComment() {
        return type.equals(GroupNotificationType.PHOTO_COMMENT);
    }

    public boolean isPhotoMarked() {
        return type.equals(GroupNotificationType.PHOTO_MARKED);
    }

    public boolean isRemoveUser() {
        return type.equals(GroupNotificationType.REMOVE_USER);
    }

    public boolean isChangeGroupOwner() {
        return type.equals(GroupNotificationType.CHANGE_GROUP_OWNER);
    }

    public boolean isChangeAlbumOwner() {
        return type.equals(GroupNotificationType.CHANGE_ALBUM_OWNER);
    }

}
