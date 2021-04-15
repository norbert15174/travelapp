package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;



@NoArgsConstructor
@Setter
@Getter
@Entity
public class LinkFriends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalData user;
    @ManyToOne
    private Friends friend;

    public LinkFriends(PersonalData user , Friends friend) {
        this.user = user;
        this.friend = friend;
    }
}
