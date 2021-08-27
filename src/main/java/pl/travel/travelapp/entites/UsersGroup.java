package pl.travel.travelapp.entites;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class UsersGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(mappedBy = "groups", cascade = {
            CascadeType.PERSIST ,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    private Set <PersonalData> members = new HashSet <>();
    private String groupName;
    private String description;
    private String groupPicture;

    //private List<GroupAlbums> groupAlbumsList;
    //private List<GroupMessages> messages;


    public void addMember(PersonalData member) {
        members.add(member);
        member.getGroups().add(this);
    }

    public void removeMember(PersonalData member) {
        members.remove(member);
        member.getGroups().remove(this);
    }

}
