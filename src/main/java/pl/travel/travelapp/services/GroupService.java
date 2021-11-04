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
import pl.travel.travelapp.DTO.groups.*;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.entites.enums.NotificationGroupStatus;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.interfaces.GroupNotificationInterface;
import pl.travel.travelapp.interfaces.GroupServiceInterface;
import pl.travel.travelapp.services.delete.interfaces.IGroupDeleteService;
import pl.travel.travelapp.services.query.interfaces.IFriendsQueryService;
import pl.travel.travelapp.services.query.interfaces.IGroupQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.save.interfaces.IGroupSaveService;
import pl.travel.travelapp.services.save.interfaces.IPersonalDataSaveService;
import pl.travel.travelapp.validators.UsersGroupValidator;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService extends UsersGroupValidator implements GroupServiceInterface {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${bucket-name}")
    private String bucket;
    @Value("${url-gcp-addr}")
    private String url;

    private final IPersonalQueryService personalQueryService;
    private final IPersonalDataSaveService personalDataSaveService;
    private final IGroupSaveService groupSaveService;
    private final IGroupQueryService groupQueryService;
    private final IFriendsQueryService friendsQueryService;
    private final GroupNotificationInterface groupNotificationService;
    private final IGroupDeleteService groupDeleteService;

    @Autowired
    public GroupService(IPersonalQueryService personalQueryService , IPersonalDataSaveService personalDataSaveService , IGroupSaveService groupSaveService , IGroupQueryService groupQueryService , IFriendsQueryService friendsQueryService , GroupNotificationInterface groupNotificationService , IGroupDeleteService groupDeleteService) {
        this.personalQueryService = personalQueryService;
        this.personalDataSaveService = personalDataSaveService;
        this.groupSaveService = groupSaveService;
        this.groupQueryService = groupQueryService;
        this.friendsQueryService = friendsQueryService;
        this.groupNotificationService = groupNotificationService;
        this.groupDeleteService = groupDeleteService;
    }

    @Transactional
    @Override
    public ResponseEntity <GroupGetDTO> create(GroupCreateDTO group , Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        ValidationErrors errors = validateCreate(group , new ValidationErrors());
        if ( errors.hasErrors() ) {
            return new ResponseEntity(errors.toString() , HttpStatus.BAD_REQUEST);
        }
        UsersGroup created = groupSaveService.create(new UsersGroup(group , user));
        user.addGroup(created);
        personalDataSaveService.update(user);

        if ( group.getMembersToAdd() != null && !group.getMembersToAdd().isEmpty() ) {
            Set <PersonalData> membersToAdd = personalQueryService.findAllById(group.getMembersToAdd());
            List <Friends> friends = friendsQueryService.findFriendsByUserId(user.getId());
            Set <PersonalData> users = new HashSet <>();
            for (Friends friend : friends) {
                users.add(friend.getSecondUser());
                users.add(friend.getFirstUser());
            }
            users.remove(user);
            Set <Long> groupRequestUserIds = created.getGroupMemberRequests().stream().map(GroupMemberRequest::getUser).map(PersonalData::getId).collect(Collectors.toSet());
            for (PersonalData member : membersToAdd) {
                if ( users.contains(member) ) {
                    if ( !groupRequestUserIds.contains(member.getId()) ) {
                        GroupMemberRequest request = new GroupMemberRequest(created , member);
                        groupSaveService.createMemberRequest(request);
                        groupNotificationService.createGroupRequest(created , member , user , request);
                    }
                }
            }
        }
        return new ResponseEntity(new GroupGetDTO(created) , HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupGetDTO> update(UpdateGroupDTO group , Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());

        UsersGroup groupToUpdate = null;
        try {
            groupToUpdate = groupQueryService.getGroupById(group.getId());
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !groupToUpdate.getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        if ( !Strings.isNullOrEmpty(group.getDescription()) ) {
            groupToUpdate.setDescription(group.getDescription());
        }
        if ( !Strings.isNullOrEmpty(group.getGroupName()) ) {
            groupToUpdate.setGroupName(group.getGroupName());
        }

        if ( group.getMembersToDelete() != null && !group.getMembersToDelete().isEmpty() ) {
            Set <PersonalData> membersToDelete = personalQueryService.findAllById(group.getMembersToDelete());
            for (PersonalData member : membersToDelete) {
                groupToUpdate.removeMember(member);
                GroupMemberRequest request = groupQueryService.getGroupMemberRequestByGroupIdAndUserId(group.getId() , member.getId());
                if ( request != null ) {
                    groupDeleteService.deleteMemberRequest(request.getId());
                }
            }
            personalDataSaveService.updateAll(membersToDelete);
        }
        if ( group.getMembersToAdd() != null && !group.getMembersToAdd().isEmpty() ) {
            Set <PersonalData> membersToAdd = personalQueryService.findAllById(group.getMembersToAdd());
            List <Friends> friends = friendsQueryService.findFriendsByUserId(user.getId());
            Set <PersonalData> users = new HashSet <>();
            for (Friends friend : friends) {
                users.add(friend.getSecondUser());
                users.add(friend.getFirstUser());
            }
            users.remove(user);
            Set <Long> groupRequestUserIds = groupToUpdate.getGroupMemberRequests().stream().map(GroupMemberRequest::getUser).map(PersonalData::getId).collect(Collectors.toSet());
            for (PersonalData member : membersToAdd) {
                if ( users.contains(member) ) {
                    if ( !groupRequestUserIds.contains(member.getId()) ) {
                        GroupMemberRequest request = new GroupMemberRequest(groupToUpdate , member);
                        groupSaveService.createMemberRequest(request);
                        groupNotificationService.createGroupRequest(groupToUpdate , member , user , request);
                    }
                }
            }
        }
        return new ResponseEntity(new GroupGetDTO(groupSaveService.update(groupToUpdate)) , HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <GroupGetDTO> getGroupById(Principal principal , Long groupId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        UsersGroup group = null;
        try {
            group = groupQueryService.getGroupById(groupId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !group.getMembers().contains(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(new GroupGetDTO(group) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity deleteGroupRequest(Principal principal , Long requestId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupMemberRequest groupMemberRequest = null;
        try {
            groupMemberRequest = groupQueryService.getGroupRequestById(requestId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if ( !groupMemberRequest.getUser().equals(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        GroupNotification groupNotification = groupNotificationService.getGroupNotificationByUserAndGroupAndRequestId(user.getId() , groupMemberRequest.getGroup().getId() , groupMemberRequest.getId());
        if ( !groupNotification.getStatus().equals(NotificationGroupStatus.ACCEPTED) ) {
            groupNotificationService.delete(groupNotification);
        }
        groupDeleteService.deleteMemberRequest(requestId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity acceptGroupRequest(Principal principal , Long requestId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        GroupMemberRequest groupMemberRequest = null;
        try {
            groupMemberRequest = groupQueryService.getGroupRequestById(requestId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if ( !groupMemberRequest.getUser().equals(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        addMembersToGroup(Collections.singleton(groupMemberRequest.getUser().getId()) , groupMemberRequest.getGroup().getOwner() , groupMemberRequest.getGroup());
        groupMemberRequest.setMember(true);
        groupSaveService.updateGroupMemberRequest(groupMemberRequest);
        GroupNotification groupNotification = groupNotificationService.getGroupNotificationByUserAndGroupAndRequestId(user.getId() , groupMemberRequest.getGroup().getId() , groupMemberRequest.getId());
        groupNotification.setStatus(NotificationGroupStatus.ACCEPTED);
        groupNotificationService.update(groupNotification);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity <List <GroupRequestDTO>> getUserGroupRequests(Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        Set <GroupRequestDTO> groupRequests = groupQueryService.getUserGroupRequests(user);
        return new ResponseEntity(groupRequests , HttpStatus.OK);
    }

    private void addMembersToGroup(Set <Long> membersIds , PersonalData user , UsersGroup groupToUpdate) {
        Set <PersonalData> membersToAdd = personalQueryService.findAllById(membersIds);
        List <Friends> friends = friendsQueryService.findFriendsByUserId(user.getId());
        Set <PersonalData> users = new HashSet <>();
        for (Friends friend : friends) {
            users.add(friend.getSecondUser());
            users.add(friend.getFirstUser());
        }
        users.remove(user);
        for (PersonalData member : membersToAdd) {
            if ( users.contains(member) ) {
                groupToUpdate.addMember(member);
            }
        }
        personalDataSaveService.updateAll(membersToAdd);
    }

    @Transactional
    @Override
    public ResponseEntity <List <GroupNotificationDTO>> getUserGroupNotification(Principal principal , Integer size , Integer page) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        return new ResponseEntity(groupNotificationService.getUserGroupNotification(user.getId() , size , page) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupGetDTO> setGroupPhoto(Principal principal , MultipartFile file , Long groupId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        UsersGroup group = null;
        try {
            group = groupQueryService.getGroupById(groupId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity <>(HttpStatus.NOT_FOUND);
        }
        if ( !group.getOwner().equals(user) ) {
            return new ResponseEntity <>(HttpStatus.FORBIDDEN);
        }
        try {
            String path = "group/" + group.getGroupName() + "/id/" + group.getId() + "/picture/main/" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucket , path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo , file.getBytes());
            group.setGroupPicture(url + path);
            return new ResponseEntity <>(new GroupGetDTO(groupSaveService.update(group)) , HttpStatus.OK);
        } catch ( IOException e ) {
            e.printStackTrace();
            return new ResponseEntity <>(HttpStatus.NOT_MODIFIED);
        }
    }

    @Transactional
    @Override
    public ResponseEntity leaveGroup(Principal principal , Long groupId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        UsersGroup group = null;
        try {
            group = groupQueryService.getGroupById(groupId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if ( !group.isMember(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if ( group.getOwner().equals(user) ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        group.removeMember(user);
        groupSaveService.update(group);
        personalDataSaveService.update(user);
        GroupMemberRequest request = groupQueryService.getGroupMemberRequestByGroupIdAndUserId(groupId , user.getId());
        if ( request != null ) {
            groupDeleteService.deleteMemberRequest(request.getId());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupGetDTO> changeOwner(Principal principal , Long groupId , Long userId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        PersonalData newOwner = null;
        UsersGroup group = null;
        try {
            group = groupQueryService.getGroupById(groupId);
            newOwner = personalQueryService.getById(userId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        if ( !group.getOwner().equals(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if ( !group.isMember(newOwner) ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        group.setOwner(newOwner);
        return new ResponseEntity <>(new GroupGetDTO(groupSaveService.update(group)) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity <GroupGetDTO> removeMemberFromGroup(Principal principal , Long groupId , Long userId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        PersonalData removeUser = null;
        UsersGroup group = null;
        try {
            group = groupQueryService.getGroupById(groupId);
            removeUser = personalQueryService.getById(userId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        if ( !group.getOwner().equals(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if ( !group.isMember(removeUser) ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        group.removeMember(removeUser);
        personalDataSaveService.update(removeUser);
        groupNotificationService.createRemoveUserFromGroup(group , removeUser);
        GroupMemberRequest request = groupQueryService.getGroupMemberRequestByGroupIdAndUserId(groupId , userId);
        if ( request != null ) {
            groupDeleteService.deleteMemberRequest(request.getId());
        }
        return new ResponseEntity <>(new GroupGetDTO(groupSaveService.update(group)) , HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity deleteGroup(Principal principal , Long groupId) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        UsersGroup group = null;
        try {
            group = groupQueryService.getGroupById(groupId);
        } catch ( NotFoundException e ) {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        if ( !group.getOwner().equals(user) ) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if ( group.getMembers().size() > 1 ) {
            return new ResponseEntity("You cannot delete as long as group has members" , HttpStatus.BAD_REQUEST);
        }
        group.removeMember(user);
        personalDataSaveService.update(user);
        groupDeleteService.delete(group);
        return new ResponseEntity <>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity <List <GroupGetDTO>> getUserGroups(Principal principal) {
        PersonalData user = personalQueryService.getPersonalInformation(principal.getName());
        return new ResponseEntity(groupQueryService.getUserGroups(user.getId()) , HttpStatus.OK);
    }

}
