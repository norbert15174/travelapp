package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.UserRegisterDTO;

public interface IUserQueryService {

    boolean checkIfExist(UserRegisterDTO user);

}
