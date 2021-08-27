package pl.travel.travelapp.entites;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.enums.MessageStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String text;
    private LocalDate date = LocalDate.now();
    @NotNull
    private String sender;
    private String photoUrl;
    private long senderId;
    @ManyToOne
    private Friends friends;
    private MessageStatus messageStatus;
}
