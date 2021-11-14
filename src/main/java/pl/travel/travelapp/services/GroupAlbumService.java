package pl.travel.travelapp.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.travel.travelapp.DTO.groups.GroupAlbumCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumFullDTO;
import pl.travel.travelapp.DTO.groups.GroupAlbumHistoryDTO;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.interfaces.GroupAlbumHistoryInterface;
import pl.travel.travelapp.interfaces.GroupAlbumInterface;
import pl.travel.travelapp.interfaces.GroupNotificationInterface;
import pl.travel.travelapp.repositories.CoordinatesRepository;
import pl.travel.travelapp.repositories.CountryRepository;
import pl.travel.travelapp.services.delete.interfaces.IGroupDeleteService;
import pl.travel.travelapp.services.query.interfaces.IGroupAlbumQueryService;
import pl.travel.travelapp.services.query.interfaces.IGroupPhotoQueryService;
import pl.travel.travelapp.services.query.interfaces.IGroupQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.save.interfaces.IGroupAlbumSaveService;
import pl.travel.travelapp.services.save.interfaces.IGroupPhotoSaveService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class GroupAlbumService implements GroupAlbumInterface {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private final IGroupPhotoQueryService groupPhotoQueryService;
    private final IGroupPhotoSaveService groupPhotoSaveService;
    private final IGroupDeleteService groupDeleteService;
    private final GroupNotificationInterface groupNotificationInterface;
    private final IGroupQueryService groupQueryService;
    private final IPersonalQueryService personalQueryService;
    private final CoordinatesRepository coordinatesRepository;
    private final CountryRepository countryRepository;
    private final GroupAlbumHistoryInterface groupAlbumHistoryService;
    private final IGroupAlbumSaveService groupAlbumSaveService;
    private final IGroupAlbumQueryService groupAlbumQueryService;

    public GroupAlbumService(IGroupPhotoQueryService groupPhotoQueryService , IGroupPhotoSaveService groupPhotoSaveService , IGroupDeleteService groupDeleteService , GroupNotificationInterface groupNotificationInterface , IGroupQueryService groupQueryService , IPersonalQueryService personalQueryService , CoordinatesRepository coordinatesRepository , CountryRepository countryRepository , GroupAlbumHistoryInterface groupAlbumHistoryService , IGroupAlbumSaveService groupAlbumSaveService , IGroupAlbumQueryService groupAlbumQueryService) {
        this.groupPhotoQueryService = groupPhotoQueryService;
        this.groupPhotoSaveService = groupPhotoSaveService;
        this.groupDeleteService = groupDeleteService;
        this.groupNotificationInterface = groupNotificationInterface;
        this.groupQueryService = groupQueryService;
        this.personalQueryService = personalQueryService;
        this.coordinatesRepository = coordinatesRepository;
        this.countryRepository = countryRepository;
        this.groupAlbumHistoryService = groupAlbumHistoryService;
        this.groupAlbumSaveService = groupAlbumSaveService;
        this.groupAlbumQueryService = groupAlbumQueryService;
    }

    @Transactional
    @Override
    public ResponseEntity <GroupAlbumDTO> create(Principal principal , GroupAlbumCreateDTO model , Long groupId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        if ( !model.canBeSave() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        UsersGroup group;
        try {
            group = groupQueryService.getGroupById(groupId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        if ( !group.getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        Optional <Country> country = countryRepository.findFirstByCountry(model.getCoordinates().getCountry());
        if ( !country.isPresent() ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        Coordinates coordinate = new Coordinates(model.getCoordinates() , country.get());
        GroupAlbum album = new GroupAlbum(user , coordinate , group , model.getDescription() , model.getName());
        GroupAlbum created = groupAlbumSaveService.save(album);
        for (PersonalData userNotification : group.getMembers()) {
            if ( !userNotification.equals(user) ) {
                groupNotificationInterface.createNewAlbum(group , userNotification , created.getId());
            }
        }
        groupAlbumHistoryService.createGroupAlbum(album , user);

        return new ResponseEntity <>(new GroupAlbumDTO(created) , HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupAlbumDTO> update(Principal principal , GroupAlbumCreateDTO model , Long groupAlbumId) {
        if ( !(model.hasValidCooridantes() || model.hasDescription() || model.hasName()) ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum groupAlbum;
        try {
            groupAlbum = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        if ( !groupAlbum.getOwner().equals(user) && !groupAlbum.getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        if ( model.hasValidCooridantes() ) {
            Optional <Country> country = countryRepository.findFirstByCountry(model.getCoordinates().getCountry());
            if ( !country.isPresent() ) {
                return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
            }
            Coordinates coordinate = new Coordinates(model.getCoordinates() , country.get());
            groupAlbum.setCoordinate(coordinate);
        }
        if ( model.hasName() ) {
            groupAlbum.setName(model.getName());
        }
        if ( model.hasDescription() ) {
            groupAlbum.setDescription(model.getDescription());
        }
        PersonalData newUser = null;
        if ( model.getNewOwnerId() != null && model.getNewOwnerId() > 0 ) {
            try {
                newUser = personalQueryService.getById(model.getNewOwnerId());
            } catch ( NotFoundException e ) {
                return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
            }
            if ( !groupAlbum.getGroup().getMembers().contains(newUser) ) {
                return new ResponseEntity <>(HttpStatus.FORBIDDEN);
            }
            groupAlbum.setOwner(newUser);
        }

        GroupAlbum updated = groupAlbumSaveService.save(groupAlbum);
        groupAlbumHistoryService.updateGroupAlbum(updated , user);
        if ( model.getNewOwnerId() != null && model.getNewOwnerId() > 0 ) {
            groupNotificationInterface.changedAlbumOwner(updated.getGroup() , user , updated.getId() , newUser);
            groupAlbumHistoryService.changeOwner(updated , user);
        }

        return new ResponseEntity <>(new GroupAlbumDTO(updated) , HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity <List <GroupAlbumHistoryDTO>> getAlbumHistoryByGroupAlbumId(Principal principal , Long groupAlbumId , Integer page) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum album;
        try {
            album = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !album.getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity <>(groupAlbumHistoryService.getAlbumHistoryByGroupAlbumId(groupAlbumId , page) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupAlbumDTO> setMainAlbumPhoto(Principal principal , MultipartFile file , Long groupAlbumId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum groupAlbum;
        try {
            groupAlbum = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        if ( !groupAlbum.getOwner().equals(user) && !groupAlbum.getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }

        String path = "group/" + groupAlbum.getGroup().getGroupName() + "/id/" + groupAlbum.getGroup().getId() + "/groupAlbum/" + groupAlbum.getId() + "/main/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket , path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo , file.getBytes());
        } catch ( IOException ioException ) {
            ioException.printStackTrace();
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }
        groupAlbum.setAlbumMainPhoto(url + path);
        GroupAlbum created = groupAlbumSaveService.save(groupAlbum);
        groupAlbumHistoryService.setGroupAlbumMainPicture(groupAlbum , user);
        return new ResponseEntity <>(new GroupAlbumDTO(created) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupAlbumDTO> setBackgroundAlbumPhoto(Principal principal , MultipartFile file , Long groupAlbumId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum groupAlbum;
        try {
            groupAlbum = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
        }
        if ( !groupAlbum.getOwner().equals(user) && !groupAlbum.getGroup().getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }

        String path = "group/" + groupAlbum.getGroup().getGroupName() + "/id/" + groupAlbum.getGroup().getId() + "/groupAlbum/" + groupAlbum.getId() + "/background/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucket , path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo , file.getBytes());
        } catch ( IOException ioException ) {
            ioException.printStackTrace();
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }
        groupAlbum.setAlbumBackgroundPhoto(url + path);
        GroupAlbum created = groupAlbumSaveService.save(groupAlbum);
        groupAlbumHistoryService.setGroupAlbumBackgroundPicture(groupAlbum , user);
        return new ResponseEntity <>(new GroupAlbumDTO(created) , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <GroupAlbumFullDTO> getGroupAlbumFullInformation(Principal principal , Long groupAlbumId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupAlbum album;
        try {
            album = groupAlbumQueryService.getGroupAlbumById(groupAlbumId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !album.getGroup().getMembers().contains(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity <>(groupAlbumQueryService.getGroupAlbumByIdWithPhotos(groupAlbumId) , HttpStatus.OK);
    }

}
