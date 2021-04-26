package pl.travel.travelapp.models;

import com.google.j2objc.annotations.ObjectiveCName;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "friend", cascade = CascadeType.REMOVE)
    private List<LinkFriends> friends;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FriendMessages messages;
    private long groupLeader = -1;
    private String name;

    public Friends(long id , List <LinkFriends> friends , FriendMessages messages , long groupLeader) {
        this.id = id;
        this.friends = friends;
        this.messages = messages;
        this.groupLeader = groupLeader;
    }
}
