package pl.travel.travelapp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.entites.enums.NotificationStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private NotificationStatus status;
    private PersonalInformationDTO user = new PersonalInformationDTO();
    private Long albumId;
    private Long photoId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public NotificationDTO(IndividualAlbum album, long id) {
        SharedAlbum sharedAlbum = album.getSharedAlbum().stream().filter(tag -> tag.getUserId() == id).findFirst().get();
        this.id = sharedAlbum.getId();
        this.user.setPhoto(album.getOwner().getProfilePicture());
        this.user.setSurName(album.getOwner().getSurName());
        this.user.setName(album.getOwner().getFirstName());
        this.user.setId(album.getOwner().getId());
        this.albumId = album.getId();
        this.dateTime = sharedAlbum.getDateTime();
        this.status = NotificationStatus.PHOTO_TAG;
    }

    public NotificationDTO(AlbumPhotos photo , long id) {
        TaggedUser taggedUser = photo.getTaggedList().stream().filter(tag -> tag.getUserId() == id).findFirst().get();
        this.id = taggedUser.getTaggedId();
        this.user.setPhoto(photo.getIndividualAlbum().getOwner().getProfilePicture());
        this.user.setSurName(photo.getIndividualAlbum().getOwner().getSurName());
        this.user.setName(photo.getIndividualAlbum().getOwner().getFirstName());
        this.user.setId(photo.getIndividualAlbum().getOwner().getId());
        this.dateTime = taggedUser.getDateTime();
        this.albumId = photo.getIndividualAlbum().getId();
        this.photoId = photo.getPhotoId();
        this.status = NotificationStatus.ALBUM_SHARE;
    }

    public NotificationDTO(Comments comment, AlbumPhotos photo) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.id = comment.getCommentId();
        this.user.setPhoto(comment.getPhoto());
        this.user.setSurName(comment.getSurName());
        this.user.setName(comment.getName());
        this.user.setId(comment.getUserId());
        this.dateTime = LocalDateTime.parse(comment.getTime(), dateTimeFormatter);
        this.albumId = photo.getIndividualAlbum().getId();
        this.photoId = photo.getPhotoId();
        this.status = NotificationStatus.COMMENT;
    }

    public NotificationDTO(FriendsRequest request) {
        this.id = request.getId();
        this.user = new PersonalInformationDTO(request.getSender());
        this.dateTime = request.getDateTime();
        this.status = NotificationStatus.FRIEND_REQUEST;
    }


}
