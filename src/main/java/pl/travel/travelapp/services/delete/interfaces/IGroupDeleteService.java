package pl.travel.travelapp.services.delete.interfaces;

import pl.travel.travelapp.entites.UsersGroup;

public interface IGroupDeleteService {

    void delete(UsersGroup group);

    void deleteMemberRequest(Long requestId);
}
