package pl.travel.travelapp.entites;


import lombok.*;
import pl.travel.travelapp.DTO.groups.GroupCreateDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    @Size(max = 50)
    private String groupName;
    @Size(max = 600)
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

    public boolean isMember(PersonalData member){
        return getMembers().contains(member);
    }

    public void addMember(PersonalData member) {
        members.add(member);
        member.getGroups().add(this);
    }

    public void removeMember(PersonalData member) {
        members.remove(member);
        member.getGroups().remove(this);
    }


}
