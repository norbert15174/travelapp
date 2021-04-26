package pl.travel.travelapp.DTO;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FriendsDTO {
    private long id;
    private String name;
    private String lastName;
    private String profilePicture;
}
