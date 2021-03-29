package pl.travel.travelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendRequestDTO {
    private long id;
    private long userId;
    private String firstName;
    private String lastName;
    private String photo;
}
