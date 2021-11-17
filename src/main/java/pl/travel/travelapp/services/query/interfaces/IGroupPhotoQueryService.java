package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.groups.GroupCommentsDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoDTO;
import pl.travel.travelapp.entites.GroupPhoto;
import pl.travel.travelapp.exceptions.NotFoundException;

import java.util.List;
import java.util.Set;

public interface IGroupPhotoQueryService {
    List <GroupPhotoDTO> getGroupPhotoByGroupAlbumId(Long groupAlbumId , Integer page);

    GroupPhoto getPhotoById(Long photoId) throws NotFoundException;

    List<GroupCommentsDTO> getPhotoCommentsByPhotoId(Long photoId);

    Set<GroupPhoto> getPhotosByIds(Set<Long> groupPhotoIds);
}
