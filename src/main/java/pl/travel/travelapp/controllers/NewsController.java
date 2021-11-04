package pl.travel.travelapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.travel.travelapp.DTO.IndividualAlbumDTO;
import pl.travel.travelapp.DTO.NotificationDTO;
import pl.travel.travelapp.interfaces.NewsInterfaces;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsInterfaces newsService;

    @Autowired
    public NewsController(NewsInterfaces newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity <List <IndividualAlbumDTO>> getNews(@RequestParam("page") Integer page , Principal principal) {
        return newsService.getActualNews(page , principal);
    }

    @GetMapping("/notification")
    public ResponseEntity <List <NotificationDTO>> getUserNotification(Principal principal) {
        return newsService.getUserNotification(principal);
    }

}
