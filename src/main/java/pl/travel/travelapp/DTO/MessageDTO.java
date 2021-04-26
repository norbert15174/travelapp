package pl.travel.travelapp.DTO;

import com.sun.istack.NotNull;
import lombok.*;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
    private long messageId;
    @NotNull
    private String text;
    @NotNull
    private String sender;
    private String photoUrl;
    private long senderId;
}
