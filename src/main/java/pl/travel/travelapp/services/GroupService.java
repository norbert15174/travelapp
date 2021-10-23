package pl.travel.travelapp.services;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.groups.GroupCreateDTO;
import pl.travel.travelapp.DTO.groups.GroupGetDTO;
import pl.travel.travelapp.DTO.groups.GroupRequestDTO;
import pl.travel.travelapp.DTO.groups.UpdateGroupDTO;
import pl.travel.travelapp.entites.*;
import pl.travel.travelapp.exceptions.NotFoundException;
import pl.travel.travelapp.interfaces.GroupServiceInterface;
import pl.travel.travelapp.services.query.interfaces.IFriendsQueryService;
import pl.travel.travelapp.services.query.interfaces.IGroupQueryService;
import pl.travel.travelapp.services.query.interfaces.IPersonalQueryService;
import pl.travel.travelapp.services.save.interfaces.IGroupSaveService;
import pl.travel.travelapp.services.save.interfaces.IPersonalDataSaveService;
import pl.travel.travelapp.validators.UsersGroupValidator;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService extends UsersGroupValidator implements GroupServiceInterface {

    private final IPersonalQueryService personalQueryService;
    private final IPersonalDataSaveService personalDataSaveService;
    private final IGroupSaveService groupSaveService;
    private final IGroupQueryService groupQueryService;
    private final IFriendsQueryService friendsQueryService;

    @Autowired
    public GroupService(IPersonalQueryService personalQueryService , IPersonalDataSaveService personalDataSaveService , IGroupSaveService groupSaveService , IGroupQueryService groupQueryService , IFriendsQueryService friendsQueryService) {
        this.personalQueryService = personalQueryService;
        this.personalDataSaveService = personalDataSaveService;
        this.groupSaveService = groupSaveService;
        this.groupQueryService = groupQueryService;
        this.friendsQueryService = friendsQueryService;
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

        if ( group.getMembersToDelete() != null && !group.getMembersToDelete().isEmpty() ) {
            Set <PersonalData> membersToDelete = personalQueryService.findAllById(group.getMembersToDelete());
            for (PersonalData member : membersToDelete) {
                groupToUpdate.removeMember(member);
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
                    }
                }
            }
        }
        if ( !Strings.isNullOrEmpty(group.getDescription()) ) {
            groupToUpdate.setDescription(group.getDescription());
        }
        if ( !Strings.isNullOrEmpty(group.getGroupName()) ) {
            groupToUpdate.setDescription(group.getDescription());
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
}
