package pl.travel.travelapp.DTO.groups;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.entites.GroupMemberRequest;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GroupRequestDTO {
    private Long id;
    private GroupGetDTO groups;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;

    public GroupRequestDTO(GroupMemberRequest request) {
        this.id = request.getId();
        this.groups = new GroupGetDTO(request.getGroup(), true);
        this.localDateTime = request.getDateTime();
    }

}
