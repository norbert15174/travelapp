package pl.travel.travelapp.mail.google.html;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
public class HtmlContent {
    public HtmlContent(){ }
    private static String link = "https://www.facebook.com/";
    private static String readHtml(String fileName,String username,String token){
        File file = new File("src\\main\\resources\\HTML\\" + fileName);
        String html = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                html += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        html = html.replaceAll("setname",username);
        html = html.replaceAll("link",link);
        return html;
    }
    public static String readHtmlRegistration(String username, String token){
        return readHtml("register.html",username,token);
    }

    public static String newPasswordHtml(String username,String token) {
        return readHtml("newpassword.html",username,token);
    }
}
