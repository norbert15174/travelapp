package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    private PersonalData firstUser;
    @OneToOne(fetch = FetchType.LAZY)
    private PersonalData secondUser;
    @OneToMany(mappedBy = "friends", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List <FriendMessages> messages;

    public Friends(long id , PersonalData firstUser , PersonalData secondUser , List <FriendMessages> messages) {
        this.id = id;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.messages = messages;
    }

    public void addMessage(FriendMessages messages){
        this.messages.add(messages);
    }

    public void deleteMessage(FriendMessages messages){
        this.messages.remove(messages);
    }

}
