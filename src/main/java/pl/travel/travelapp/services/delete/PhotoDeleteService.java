package pl.travel.travelapp.services.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.models.AlbumPhotos;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;
import pl.travel.travelapp.services.delete.interfaces.IPhotoDeleteService;

@Service
public class PhotoDeleteService implements IPhotoDeleteService {

    private final AlbumPhotosRepository albumPhotosRepository;

    @Autowired
    public PhotoDeleteService(AlbumPhotosRepository albumPhotosRepository) {
        this.albumPhotosRepository = albumPhotosRepository;
    }

    @Override
    public void delete(AlbumPhotos photo) {
        albumPhotosRepository.delete(photo);
    }

    @Override
    public void deleteById(Long id) {
        albumPhotosRepository.deleteById(id);
    }
}
