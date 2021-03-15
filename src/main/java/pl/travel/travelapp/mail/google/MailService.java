package pl.travel.travelapp.mail.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class MailService {
    @Value("${main-email}")
    private String userMail;
    @Value("${mail-client-id}")
    private String clientId;
    @Value("${mail-client-secret}")
    private String clientSecret;
    @Value("${mail-access-token}")
    private String accessToken;
    @Value("${mail-refresh-token}")
    private String refreshToken;

    public MailService(){ }

    public boolean sendMailByGoogleMailApi(String to,String topic, String body){

        try {
            GmailService gmailService = new GmailServiceImpl(GoogleNetHttpTransport.newTrustedTransport());
            gmailService.setGmailCredentials(GmailCredentials.builder()
                    .userEmail(this.userMail)
                    .clientId(this.clientId)
                    .clientSecret(this.clientSecret)
                    .accessToken(this.accessToken)
                    .refreshToken(this.refreshToken)
                    .build());

            gmailService.sendMessage(to, topic, body);
            return true;
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
