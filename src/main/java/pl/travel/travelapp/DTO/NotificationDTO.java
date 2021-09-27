package pl.travel.travelapp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.FriendsRequest;
import pl.travel.travelapp.entites.SharedAlbum;
import pl.travel.travelapp.entites.TaggedUser;
import pl.travel.travelapp.entites.enums.NotificationStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private NotificationStatus status;
    private PersonalInformationDTO user = new PersonalInformationDTO();
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public NotificationDTO(TaggedUser user) {
        this.id = user.getTaggedId();
        this.user.setPhoto(user.getPhoto());
        this.user.setSurName(user.getSurName());
        this.user.setName(user.getName());
        this.user.setId(user.getUserId());
        this.dateTime = user.getDateTime();
        this.status = NotificationStatus.PHOTO_TAG;
    }

    public NotificationDTO(SharedAlbum user) {
        this.id = user.getId();
        this.user.setPhoto(user.getPhoto());
        this.user.setSurName(user.getSurName());
        this.user.setName(user.getName());
        this.user.setId(user.getUserId());
        this.dateTime = user.getDateTime();
        this.status = NotificationStatus.ALBUM_SHARE;
    }

    public NotificationDTO(FriendsRequest request) {
        this.id = request.getId();
        this.user = new PersonalInformationDTO(request.getSender());
        this.dateTime = request.getDateTime();
        this.status = NotificationStatus.FRIEND_REQUEST;
    }


}
