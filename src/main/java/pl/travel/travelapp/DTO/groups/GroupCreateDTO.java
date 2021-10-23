package pl.travel.travelapp.DTO.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GroupCreateDTO {

    private String groupName;
    private String description;

}
