package artur.goz.userservice.services;

import artur.goz.userservice.dto.LoginDTO;
import artur.goz.userservice.dto.UserDTO;
import artur.goz.userservice.exception.EntityAlreadyExistsException;
import artur.goz.userservice.exception.EntityNotFoundException;
import artur.goz.userservice.exception.LimitException;
import artur.goz.userservice.exception.StatusCode;
import artur.goz.userservice.mapper.UserMapper;
import artur.goz.userservice.models.User;
import artur.goz.userservice.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepo.existsByName(userDTO.getName())) {
            throw new EntityAlreadyExistsException("User already exists", StatusCode.DUPLICATE_USERNAME.name());
        }
        User userNew = userMapper.toUser(userDTO);
        userNew.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User newUser = userRepo.save(userNew);
        return userMapper.toUserDTO(newUser);
    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {
        log.info("Login attempt {}", loginDTO);
        User user = extractUserByName(loginDTO.getName());
        if (!passwordEncoder.matches(loginDTO.getPassword(),user.getPassword())) {
            throw new EntityNotFoundException("Wrong password",
                    StatusCode.WRONG_PASSWORD.name()
            );
        }
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(String username, UserDTO userDTO) {
        User user = extractUserByName(username);
        User newUser = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setRoles(newUser.getRoles());
        User userUpdated = userRepo.save(newUser);
        return userMapper.toUserDTO(userUpdated);
    }

    @Override
    public UserDTO getUserByName(String name) {
        User user = extractUserByName(name);

        return userMapper.toUserDTO(user);
    }

    @Override
    public void decrementUserLimit(String name) {
        User user = extractUserByName(name);

        if(user.getMonthlyLimit() <= 0)
            throw new LimitException("Limit exceeded", StatusCode.LIMIT_EXCEEDED.name());

        user.setMonthlyLimit(user.getMonthlyLimit() - 1);
        userRepo.save(user);
    }

    private User extractUserByName(String name) {
        return userRepo.findByName(name).orElseThrow(() ->
                new EntityNotFoundException("User with name " + name + " not found",
                StatusCode.ENTITY_NOT_FOUND.name()
                ));
    }
}
