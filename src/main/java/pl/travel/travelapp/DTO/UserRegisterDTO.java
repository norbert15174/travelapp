package pl.travel.travelapp.DTO;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @NotNull
    @Min(3)
    @Max(18)
    private String login;
    @NotNull
    @Min(3)
    @Max(18)
    private String password;
    @NotNull
    @Min(3)
    @Max(18)
    @Email
    private String email;
    @NotNull
    @Min(2)
    @Max(18)
    private String firstName;
    @NotNull
    @Min(2)
    @Max(30)
    private String surName;
    private String birthDay;
    @NotNull
    @Min(3)
    @Max(40)
    private String nationality;
}
