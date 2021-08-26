package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.models.AlbumPhotos;

import java.util.Optional;

public interface IPhotoQueryService {

    Optional <AlbumPhotos> findById(Long id);
}
