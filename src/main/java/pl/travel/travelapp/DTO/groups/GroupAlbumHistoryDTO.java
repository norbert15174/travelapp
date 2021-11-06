package pl.travel.travelapp.DTO.groups;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.travel.travelapp.DTO.PersonalInformationDTO;
import pl.travel.travelapp.entites.GroupAlbumHistory;
import pl.travel.travelapp.entites.enums.GroupAlbumHistoryStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupAlbumHistoryDTO {

    private Long id;
    private PersonalInformationDTO user;
    private GroupAlbumHistoryStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    public GroupAlbumHistoryDTO(GroupAlbumHistory history) {
        this.id = history.getId();
        this.user = new PersonalInformationDTO(history.getUser());
        this.dateTime = history.getDateTime();
        this.status = history.getStatus();
    }

}
