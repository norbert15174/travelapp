package pl.travel.travelapp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sun.istack.NotNull;
import lombok.*;
import pl.travel.travelapp.entites.FriendMessages;
import pl.travel.travelapp.entites.enums.MessageStatus;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
    private Long id;
    private Long friendsId;
    private Long senderId;
    @NotNull
    private String text;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime date;
    private MessageStatus messageStatus = MessageStatus.NEW;


    public MessageDTO(FriendMessages messages) {
        this.friendsId = messages.getFriends().getId();
        this.senderId = messages.getSender().getId();
        this.text = messages.getText();
        this.date = messages.getDate();
        this.messageStatus = messages.getMessageStatus();
        this.id = messages.getId();
    }
}
