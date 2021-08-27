package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.entites.AlbumPhotos;

public interface IPhotoDeleteService {

    void delete(AlbumPhotos photo);

    void deleteById(Long id);
}
