package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.MessageDTO;
import pl.travel.travelapp.entites.enums.MessageStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(max = 500)
    @NotNull
    private String text;
    @Column(columnDefinition = "TIMESTAMP")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime date = LocalDateTime.now();
    @OneToOne
    private PersonalData sender;
    @ManyToOne
    private Friends friends;
    private MessageStatus messageStatus = MessageStatus.NEW;

    public FriendMessages(PersonalData user , Friends friendToSave , MessageDTO messageDTO) {
        this.friends = friendToSave;
        this.sender = user;
        this.text = messageDTO.getText();
        this.date = LocalDateTime.now();
    }

    public FriendMessages setStatusRecived() {
        this.messageStatus = MessageStatus.RECEIVED;
        return this;
    }
}
