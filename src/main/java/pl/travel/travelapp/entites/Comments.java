package pl.travel.travelapp.entites;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String name;
    private String surName;
    private String photo;
    private String text;
    private String time;
    private Long userId;

    public Comments(String text , PersonalData personalData) {
        this.name = personalData.getFirstName();
        this.surName = personalData.getSurName();
        this.photo = personalData.getProfilePicture();
        this.text = text;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        this.time = time.format(dateTimeFormatter);
        this.userId = personalData.getId();
    }
}
