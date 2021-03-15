package pl.travel.travelapp.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDTO {
    private String login;
    private String password;
    private String token;
    private String role;
}
