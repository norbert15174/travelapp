package pl.travel.travelapp.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class GroupPhotoTagged {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private GroupPhoto photo;
    @OneToOne
    private PersonalData tagged;

    public GroupPhotoTagged(GroupPhoto photo , PersonalData tagged) {
        this.photo = photo;
        this.tagged = tagged;
    }
}
