package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.entites.GroupAlbum;
import pl.travel.travelapp.exceptions.NotFoundException;

public interface IGroupAlbumQueryService {
    GroupAlbum getGroupAlbumById(Long groupAlbumId) throws NotFoundException;
}
