package pl.travel.travelapp.services;

import org.springframework.stereotype.Service;

@Service
public class AlbumPhotosService {
    private AlbumPhotosService albumPhotosService;

    public AlbumPhotosService(AlbumPhotosService albumPhotosService) {
        this.albumPhotosService = albumPhotosService;
    }
}
