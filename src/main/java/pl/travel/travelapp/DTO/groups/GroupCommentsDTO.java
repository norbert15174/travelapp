package pl.travel.travelapp.DTO.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.GroupPhotoComments;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupCommentsDTO {
    private Long commentId;
    private String name;
    private String surName;
    private String photo;
    private String text;
    private String time;
    private Long userId;


    public GroupCommentsDTO(GroupPhotoComments comment) {
        this.commentId = comment.getId();
        this.text = comment.getComment();
        this.name = comment.getCommentedBy().getFirstName();
        this.surName = comment.getCommentedBy().getSurName();
        this.photo = comment.getCommentedBy().getProfilePicture();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.time = comment.getDateTime().format(dateTimeFormatter);
        this.userId = comment.getCommentedBy().getId();
    }
}
