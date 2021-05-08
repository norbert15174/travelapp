package pl.travel.travelapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;

@Service
public class AlbumPhotosService {
    private AlbumPhotosRepository albumPhotosRepository;
    @Autowired
    public AlbumPhotosService(AlbumPhotosRepository albumPhotosRepository) {
        this.albumPhotosRepository = albumPhotosRepository;
    }
}
