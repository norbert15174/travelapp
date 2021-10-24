package pl.travel.travelapp.entites;

import javax.persistence.*;

@Entity
public class GroupAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String albumMainPhoto;
    private String albumBackgroundPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersGroup group;

    @OneToOne
    private PersonalData owner;

}
