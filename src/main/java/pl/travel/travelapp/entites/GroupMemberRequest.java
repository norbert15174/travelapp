package pl.travel.travelapp.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
public class GroupMemberRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean isMember = false;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData user;
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersGroup group;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public GroupMemberRequest(UsersGroup group , PersonalData user) {
        this.user = user;
        this.group = group;
        this.dateTime = LocalDateTime.now();
    }
}
