package pl.travel.travelapp.DTO.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGroupDTO {
    private Long id;
    private String groupName;
    private String description;
    private Set <Long> membersToAdd;
    private Set <Long> membersToDelete;

}
