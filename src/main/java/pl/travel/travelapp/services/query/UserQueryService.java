package pl.travel.travelapp.services.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.travel.travelapp.DTO.UserRegisterDTO;
import pl.travel.travelapp.entites.User;
import pl.travel.travelapp.repositories.UserRepository;
import pl.travel.travelapp.services.query.interfaces.IUserQueryService;

import java.util.Optional;

@Service
public class UserQueryService implements IUserQueryService {

    private final UserRepository userRepository;

    @Autowired
    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkIfExist(UserRegisterDTO user) {
        return userRepository.checkIfExist(user.getLogin() , user.getEmail()).isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional <User> findFirstByLogin(String username) {
        return userRepository.findFirstByLogin(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional <User> findFirstByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByLogin(String s) {
        return userRepository.findByLogin(s);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByUsernameAllInformation(String username) {
        return userRepository.findUserByUsernameAllInformation(username);
    }
}
