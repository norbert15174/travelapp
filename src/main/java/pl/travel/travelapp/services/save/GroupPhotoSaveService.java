package pl.travel.travelapp.services.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupPhotoAlbumEnterDTO;
import pl.travel.travelapp.entites.GroupPhoto;
import pl.travel.travelapp.entites.GroupPhotoComments;
import pl.travel.travelapp.entites.GroupPhotoTagged;
import pl.travel.travelapp.repositories.GroupPhotoCommentRepository;
import pl.travel.travelapp.repositories.GroupPhotoRepository;
import pl.travel.travelapp.repositories.GroupPhotoTaggedRepository;
import pl.travel.travelapp.services.save.interfaces.IGroupPhotoSaveService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupPhotoSaveService implements IGroupPhotoSaveService {

    private final GroupPhotoRepository groupPhotoRepository;
    private final GroupPhotoTaggedRepository groupPhotoTaggedRepository;
    private final GroupPhotoCommentRepository groupPhotoCommentRepository;

    @Autowired
    public GroupPhotoSaveService(GroupPhotoRepository groupPhotoRepository , GroupPhotoTaggedRepository groupPhotoTaggedRepository , GroupPhotoCommentRepository groupPhotoCommentRepository) {
        this.groupPhotoRepository = groupPhotoRepository;
        this.groupPhotoTaggedRepository = groupPhotoTaggedRepository;
        this.groupPhotoCommentRepository = groupPhotoCommentRepository;
    }

    @Transactional
    @Override
    public Set <GroupPhotoAlbumEnterDTO> saveAll(Set <GroupPhoto> groupPhotos) {
        return groupPhotoRepository.saveAll(groupPhotos).stream().map(GroupPhotoAlbumEnterDTO::new).collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public GroupPhotoAlbumEnterDTO save(GroupPhoto groupPhoto) {
        return new GroupPhotoAlbumEnterDTO(groupPhotoRepository.save(groupPhoto));
    }

    @Override
    public GroupPhotoTagged saveTagged(GroupPhotoTagged tagged) {
        return groupPhotoTaggedRepository.save(tagged);
    }

    @Override
    public void saveComment(GroupPhotoComments comment) {
        groupPhotoCommentRepository.save(comment);
    }
}
