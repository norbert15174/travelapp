package pl.travel.travelapp.services.save.interfaces;

import pl.travel.travelapp.entites.GroupMemberRequest;
import pl.travel.travelapp.entites.UsersGroup;

public interface IGroupSaveService {
    UsersGroup create(UsersGroup group);

    UsersGroup update(UsersGroup groupToUpdate);

    void createMemberRequest(GroupMemberRequest request);

    void updateGroupMemberRequest(GroupMemberRequest request);
}
