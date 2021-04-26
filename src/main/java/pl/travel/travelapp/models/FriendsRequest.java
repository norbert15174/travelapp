package pl.travel.travelapp.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FriendsRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean isFriends = false;
    @OneToOne(fetch = FetchType.LAZY)
    private PersonalData sender;
    private long receiver;

    public FriendsRequest(PersonalData sender, long receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
