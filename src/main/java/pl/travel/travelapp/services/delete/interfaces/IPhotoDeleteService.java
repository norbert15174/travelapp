package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.models.AlbumPhotos;

public interface IPhotoDeleteService {

    void delete(AlbumPhotos photo);

    void deleteById(Long id);
}
