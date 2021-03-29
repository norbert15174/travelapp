package pl.travel.travelapp.models;

import com.google.j2objc.annotations.ObjectiveCName;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "friend")
    private List<LinkFriends> friends;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FriendMessages messages;
}
