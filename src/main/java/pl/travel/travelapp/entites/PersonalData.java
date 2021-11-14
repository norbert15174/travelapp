package pl.travel.travelapp.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class PersonalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 20)
    private String firstName;
    @Size(min = 3, max = 20)
    private String surName;
    private long phoneNumber;
    private String profilePicture;
    private String backgroundPicture;

    private LocalDate birthDate;
    @OneToOne(fetch = FetchType.EAGER)
    private Country Nationality;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PersonalDescription personalDescription;

    @ManyToMany
    @JoinTable(name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set <UsersGroup> groups = new HashSet <>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List <IndividualAlbum> albums = new ArrayList();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupMemberRequest> groupMemberRequests;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set <GroupNotification> groupNotifications;


    public void addGroup(UsersGroup group) {
        groups.add(group);
        group.getMembers().add(this);
    }

    public void removeGroup(UsersGroup group) {
        groups.remove(group);
        group.getMembers().remove(this);
    }

    public void addAlbum(IndividualAlbum album) {
        albums.add(album);
    }

    public void removeAlbum(IndividualAlbum album) {
        albums.remove(album);
        album.setOwner(null);
    }


    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        PersonalData that = (PersonalData) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
