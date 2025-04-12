package artur.goz.userservice.services;

import artur.goz.userservice.dto.LoginDTO;
import artur.goz.userservice.dto.UserDTO;
import artur.goz.userservice.models.User;

import java.util.UUID;

public interface UserService {
    UserDTO register(UserDTO userDTO);
    UserDTO login(LoginDTO loginDTO);
    UserDTO updateUser(String name, UserDTO userDTO);
    void decrementUserLimit(String name);
}
