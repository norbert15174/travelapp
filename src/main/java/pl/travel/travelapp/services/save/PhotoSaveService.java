package pl.travel.travelapp.services.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.travel.travelapp.entites.AlbumPhotos;
import pl.travel.travelapp.repositories.AlbumPhotosRepository;
import pl.travel.travelapp.services.save.interfaces.IPhotoSaveService;

@Service
public class PhotoSaveService implements IPhotoSaveService {

    private final AlbumPhotosRepository albumPhotosRepository;

    @Autowired
    public PhotoSaveService(AlbumPhotosRepository albumPhotosRepository) {
        this.albumPhotosRepository = albumPhotosRepository;
    }

    @Override
    public AlbumPhotos save(AlbumPhotos album) {
        return albumPhotosRepository.save(album);
    }
}
