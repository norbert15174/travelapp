package pl.travel.travelapp.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String albumMainPhoto;
    private String albumBackgroundPhoto;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersGroup group;

    @OneToOne
    private PersonalData owner;

}
