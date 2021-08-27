package pl.travel.travelapp.services.query.interfaces;

import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.entites.User;

import java.util.Optional;

public interface IUserQueryService {

    boolean checkIfExist(UserRegisterDTO user);

    Optional <User> findFirstByLogin(String username);

    Optional <User> findFirstByEmail(String email);

    User findByLogin(String s);

    User findUserByUsernameAllInformation(String username);
}
