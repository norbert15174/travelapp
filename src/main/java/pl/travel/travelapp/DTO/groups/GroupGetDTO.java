package pl.travel.travelapp.DTO.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.UsersGroup;
import pl.travel.travelapp.mappers.PersonalDataToPersonalDataInformationMapper;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GroupGetDTO {
    private Long id;
    private String groupName;
    private String description;
    private String groupPicture;
    private Set <PersonalInformationDTO> members = new HashSet <>();
    private PersonalInformationDTO owner;

    public GroupGetDTO(UsersGroup group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.groupPicture = group.getGroupPicture();
        members.addAll(PersonalDataToPersonalDataInformationMapper.mapPersonalDataToPersonalInformationDTO(group.getMembers()));
        this.owner = PersonalDataToPersonalDataInformationMapper.mapPersonalDataToPersonalInformationDTO(group.getOwner());
    }

    public GroupGetDTO(UsersGroup group, boolean withoutMembers) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.groupPicture = group.getGroupPicture();
        if(!withoutMembers){
            members.addAll(PersonalDataToPersonalDataInformationMapper.mapPersonalDataToPersonalInformationDTO(group.getMembers()));
        }
        this.owner = PersonalDataToPersonalDataInformationMapper.mapPersonalDataToPersonalInformationDTO(group.getOwner());
    }
}
