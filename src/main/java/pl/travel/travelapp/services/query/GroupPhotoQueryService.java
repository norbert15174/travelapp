package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupCommentsDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoDTO;
import pl.travel.travelapp.entites.GroupPhoto;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.repositories.GroupPhotoCommentRepository;
import pl.travel.travelapp.repositories.GroupPhotoRepository;
import pl.travel.travelapp.services.query.interfaces.IGroupPhotoQueryService;

import java.util.List;

@Service
public class GroupPhotoQueryService implements IGroupPhotoQueryService {

    private final GroupPhotoRepository groupPhotoRepository;
    private final GroupPhotoCommentRepository groupPhotoCommentRepository;

    @Autowired
    public GroupPhotoQueryService(GroupPhotoRepository groupPhotoRepository , GroupPhotoCommentRepository groupPhotoCommentRepository) {
        this.groupPhotoRepository = groupPhotoRepository;
        this.groupPhotoCommentRepository = groupPhotoCommentRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public List <GroupPhotoDTO> getGroupPhotoByGroupAlbumId(Long groupAlbumId , Integer page) {
        return groupPhotoRepository.getGroupPhotoDTOByGroupAlbumId(groupAlbumId , PageRequest.of(page , 9));
    }

    @Override
    public GroupPhoto getPhotoById(Long photoId) throws NotFoundException {
        return groupPhotoRepository.getPhotoById(photoId).orElseThrow(NotFoundException::new);
    }

    @Override
    public List <GroupCommentsDTO> getPhotoCommentsByPhotoId(Long photoId) {
        return groupPhotoCommentRepository.findPhotoCommentsByPhotoId(photoId);
    }
}
