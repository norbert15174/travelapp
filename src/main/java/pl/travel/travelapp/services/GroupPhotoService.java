package pl.travel.travelapp.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.CommentGroupCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupCommentsDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoAlbumEnterDTO;
import pl.travel.travelapp.DTO.groups.GroupPhotoDTO;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.interfaces.GroupAlbumHistoryInterface;
import pl.travel.travelapp.interfaces.GroupChangeInterface;
import pl.travel.travelapp.interfaces.GroupNotificationInterface;
import pl.travel.travelapp.interfaces.GroupPhotoInterface;
import pl.travel.travelapp.services.delete.interfaces.IGroupPhotoDeleteService;
import pl.travel.travelapp.services.query.interfaces.IGroupAlbumQueryService;
import pl.travel.travelapp.services.query.interfaces.IGroupPhotoQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.save.interfaces.IGroupPhotoSaveService;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupPhotoService implements GroupPhotoInterface, GroupChangeInterface {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private final IGroupPhotoDeleteService groupPhotoDeleteService;
    private final IGroupPhotoQueryService groupPhotoQueryService;
    private final IGroupPhotoSaveService groupPhotoSaveService;
    private final GroupNotificationInterface groupNotificationService;
    private final IPersonalQueryService personalQueryService;
    private final GroupAlbumHistoryInterface groupAlbumHistoryService;
    private final IGroupAlbumQueryService groupAlbumQueryService;

    @Autowired
    public GroupPhotoService(IGroupPhotoDeleteService groupPhotoDeleteService , IGroupPhotoQueryService groupPhotoQueryService , IGroupPhotoSaveService groupPhotoSaveService , GroupNotificationInterface groupNotificationService , IPersonalQueryService personalQueryService , GroupAlbumHistoryInterface groupAlbumHistoryService , IGroupAlbumQueryService groupAlbumQueryService) {
        this.groupPhotoDeleteService = groupPhotoDeleteService;
        this.groupPhotoQueryService = groupPhotoQueryService;
        this.groupPhotoSaveService = groupPhotoSaveService;
        this.groupNotificationService = groupNotificationService;
        this.personalQueryService = personalQueryService;
        this.groupAlbumHistoryService = groupAlbumHistoryService;
        this.groupAlbumQueryService = groupAlbumQueryService;
    }

    @Transactional
    @Override
    public ResponseEntity <Set <GroupPhotoAlbumEnterDTO>> addAlbumPhotos(Principal principal , MultipartFile[] files , Long groupAlbumId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum groupAlbum;
        try {
            groupAlbum = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !groupAlbum.getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        Set <GroupPhoto> groupPhotos = new HashSet <>();
        for (MultipartFile file : files) {
            GroupPhoto photo = new GroupPhoto();
            photo.setAlbum(groupAlbum);
            photo.setOwner(user);
            photo.setDateTime(LocalDateTime.now());
            String photoUrl;
            try {
                photoUrl = addPhoto(file , groupAlbum.getGroup().getGroupName() , groupAlbum.getGroup().getId() , groupAlbumId);
            } catch ( IOException e ) {
                return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
            }
            photo.setPhoto(url + photoUrl);
            groupPhotos.add(photo);
        }
        Set <GroupPhotoAlbumEnterDTO> crated = groupPhotoSaveService.saveAll(groupPhotos);
        groupAlbumHistoryService.addNewPhotos(groupAlbum , user);

        return new ResponseEntity <>(crated , HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupPhotoAlbumEnterDTO> addAlbumPhoto(Principal principal , MultipartFile file , Long groupAlbumId , String description) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum groupAlbum;
        try {
            groupAlbum = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !groupAlbum.getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        GroupPhoto photo = new GroupPhoto();
        photo.setAlbum(groupAlbum);
        photo.setOwner(user);
        photo.setDateTime(LocalDateTime.now());
        String photoUrl;
        try {
            photoUrl = addPhoto(file , groupAlbum.getGroup().getGroupName() , groupAlbum.getGroup().getId() , groupAlbumId);
        } catch ( IOException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }
        photo.setPhoto(url + photoUrl);
        photo.setDescription(description);
        GroupPhotoAlbumEnterDTO crated = groupPhotoSaveService.save(photo);
        groupAlbumHistoryService.addNewPhoto(groupAlbum , user);

        return new ResponseEntity <>(crated , HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <GroupPhotoDTO>> getGroupPhotoByGroupAlbumId(Principal principal , Long groupAlbumId , Integer page) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum groupAlbum;
        try {
            groupAlbum = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !groupAlbum.getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        List <GroupPhotoDTO> photos = groupPhotoQueryService.getGroupPhotoByGroupAlbumId(groupAlbumId , page);
        return new ResponseEntity <>(photos , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <GroupPhotoDTO> getGroupPhotoDTOByGroupPhotoId(Principal principal , Long photoId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupPhoto photo;
        try {
            photo = groupPhotoQueryService.getPhotoById(photoId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !photo.getAlbum().getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity <>(new GroupPhotoDTO(photo) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupPhotoDTO> updatePhoto(Principal principal , Long photoId , String description) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupPhoto photo;
        try {
            photo = groupPhotoQueryService.getPhotoById(photoId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !photo.getOwner().equals(user) && !photo.getAlbum().getOwner().equals(user) && !photo.getAlbum().getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        photo.setDescription(description);
        groupPhotoSaveService.save(photo);
        return new ResponseEntity <>(new GroupPhotoDTO(photo) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupPhotoDTO> tagUsers(Principal principal , Long photoId , Set <Long> usersToTag) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupPhoto photo;
        try {
            photo = groupPhotoQueryService.getPhotoById(photoId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !photo.getOwner().equals(user) && !photo.getAlbum().getOwner().equals(user) && !photo.getAlbum().getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        if ( usersToTag == null || usersToTag.isEmpty() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        Set <PersonalData> users = personalQueryService.getUsersByIds(usersToTag);
        Set <PersonalData> members = photo.getAlbum().getGroup().getMembers();
        if ( !photo.getTagged().isEmpty() ) {
            Set <PersonalData> existTaggedUsers = photo.getTagged().stream().map(GroupPhotoTagged::getTagged).collect(Collectors.toSet());
            for (PersonalData userToTag : users) {
                if ( !members.contains(userToTag) ) {
                    continue;
                }
                if ( !existTaggedUsers.contains(userToTag) ) {
                    GroupPhotoTagged tagged = new GroupPhotoTagged(photo , userToTag);
                    GroupPhotoTagged created = groupPhotoSaveService.saveTagged(tagged);
                    photo.addTagged(created);
                    groupNotificationService.tagUser(photo.getAlbum().getGroup() , created.getTagged() , photo.getAlbum().getId() , photo.getId() , user);
                }
            }
        } else {
            for (PersonalData userToTag : users) {
                if ( !members.contains(userToTag) ) {
                    continue;
                }
                GroupPhotoTagged tagged = new GroupPhotoTagged(photo , userToTag);
                GroupPhotoTagged created = groupPhotoSaveService.saveTagged(tagged);
                photo.addTagged(created);
                if ( !userToTag.equals(photo.getOwner()) ) {
                    groupNotificationService.tagUser(photo.getAlbum().getGroup() , created.getTagged() , photo.getAlbum().getId() , photo.getId() , user);
                }
            }
        }

        return new ResponseEntity <>(new GroupPhotoDTO(photo) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupPhotoDTO> unTagUsers(Principal principal , Long photoId , Set <Long> usersToUntag) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupPhoto photo;
        try {
            photo = groupPhotoQueryService.getPhotoById(photoId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !photo.getOwner().equals(user) && !photo.getAlbum().getOwner().equals(user) && !photo.getAlbum().getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        if ( usersToUntag == null || usersToUntag.isEmpty() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        Set <PersonalData> users = personalQueryService.getUsersByIds(usersToUntag);
        if ( photo.getTagged().isEmpty() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        Set <GroupPhotoTagged> untag = photo.getTagged().stream().filter(tagged -> users.contains(tagged.getTagged())).collect(Collectors.toSet());
        if ( untag.isEmpty() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        photo.untag(untag);
        groupPhotoDeleteService.untag(untag);
        groupNotificationService.deleteAllByUserIdAndPhotoId(untag
                .stream()
                .map(GroupPhotoTagged::getTagged)
                .collect(Collectors.toSet()) , photoId);

        return new ResponseEntity <>(new GroupPhotoDTO(photo) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupCommentsDTO> addComments(Principal principal , Long photoId , CommentGroupCreateDTO commentGroupCreateDTO) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        if ( Strings.isNullOrEmpty(commentGroupCreateDTO.getText()) ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        GroupPhoto photo;
        try {
            photo = groupPhotoQueryService.getPhotoById(photoId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !photo.getAlbum().getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        GroupPhotoComments comment = new GroupPhotoComments(user , photo , commentGroupCreateDTO.getText());
        groupPhotoSaveService.saveComment(comment);
        if ( !user.equals(photo.getOwner()) ) {
            groupNotificationService.createCommentNotificationIfNeeded(photo.getAlbum().getGroup() , photo.getAlbum().getOwner() , photo.getAlbum().getId() , photo.getId() , user);
        }

        List <GroupCommentsDTO> groupComments = groupPhotoQueryService.getPhotoCommentsByPhotoId(photoId);
        return new ResponseEntity(groupComments , HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity deletePhoto(Principal principal , Long groupPhotoId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupPhoto photo;
        try {
            photo = groupPhotoQueryService.getPhotoById(groupPhotoId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !photo.getOwner().equals(user) && !photo.getAlbum().getOwner().equals(user) && !photo.getAlbum().getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        groupPhotoDeleteService.delete(photo);
        return new ResponseEntity(HttpStatus.OK);
    }

    private String addPhoto(MultipartFile file , String groupName , Long groupId , Long albumId) throws IOException {
        String path = "group/id/" + groupId + "/picture/album/" + albumId + "/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket , path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo , file.getBytes());
        return path;
    }


}
