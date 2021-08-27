package pl.travel.travelapp.services.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;
import pl.travel.travelapp.services.query.interfaces.IPhotoQueryService;

import java.util.Optional;

@Service
public class PhotoQueryService implements IPhotoQueryService {

    private final AlbumPhotosRepository albumPhotosRepository;

    public PhotoQueryService(AlbumPhotosRepository albumPhotosRepository) {
        this.albumPhotosRepository = albumPhotosRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional <AlbumPhotos> findById(Long id) {
        return albumPhotosRepository.findById(id);
    }
}
