package pl.travel.travelapp.entites;


import lombok.*;
import pl.travel.travelapp.DTO.groups.GroupCreateDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "groups")
    private Set <PersonalData> members = new HashSet <>();
    @OneToOne(fetch = FetchType.LAZY)
    private PersonalData owner;
    private String groupName;
    private String description;
    private String groupPicture;

    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupMemberRequest> groupMemberRequests = new HashSet <>();
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupNotification> groupNotification = new HashSet <>();

    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupAlbum> groupAlbum = new HashSet <>();

    public UsersGroup(GroupCreateDTO group , PersonalData user) {
        this.description = group.getDescription();
        this.groupName = group.getGroupName();
        this.owner = user;
    }

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
