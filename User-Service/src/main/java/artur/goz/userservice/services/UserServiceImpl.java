package artur.goz.userservice.services;

import artur.goz.userservice.dto.LoginDTO;
import artur.goz.userservice.dto.UserDTO;
import artur.goz.userservice.exception.EntityAlreadyExistsException;
import artur.goz.userservice.exception.EntityNotFoundException;
import artur.goz.userservice.exception.StatusCode;
import artur.goz.userservice.mapper.UserMapper;
import artur.goz.userservice.models.User;
import artur.goz.userservice.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    private boolean doPasswordMatch(String userPassword, String passwordToMatch) {
        return passwordEncoder.matches(userPassword, passwordToMatch);
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepo.existsByName(userDTO.getName())) {
            throw new EntityAlreadyExistsException("User already exists", StatusCode.DUPLICATE_USERNAME.name());
        }
        User userNew = userMapper.toUser(userDTO);
        userNew.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        //  myUserNew.setRoles(Set.of("ROLE_USER"));
        User newUser = userRepo.save(userNew);
        return userMapper.toUserDTO(newUser);
    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {
        User user = getUserByName(loginDTO.getUsername());
        if (!doPasswordMatch(user.getPassword(), loginDTO.getPassword())) {
            throw new EntityNotFoundException("Wrong password",
                    StatusCode.WRONG_PASSWORD.name()
            );
        }
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(String username, UserDTO userDTO) {
        User user = getUserByName(username);
        User newUser = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setRoles(newUser.getRoles());
        User userUpdated = userRepo.save(newUser);
        return userMapper.toUserDTO(userUpdated);
    }

    @Override
    public User getUserByName(String name) {
        return userRepo.findByName(name).orElseThrow(() ->
                new EntityNotFoundException("User with name " + name + " not found",
                StatusCode.ENTITY_NOT_FOUND.name()
                ));
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found",
                StatusCode.ENTITY_NOT_FOUND.name()));
    }

    @Override
    public void decrementUserLimit(String name) {
        User user = getUserByName(name);
        user.setMonthlyLimit(user.getMonthlyLimit() - 1);
        userRepo.save(user);
    }
}
