package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@AllArgsConstructor
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
    @ManyToOne(cascade = CascadeType.ALL)
    private Friends friend;
}
