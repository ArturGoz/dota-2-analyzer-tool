package artur.goz.authservice.services;

import artur.goz.authservice.dto.*;
import artur.goz.authservice.rabbitmq.RabbitManager;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTGenerator jwtGenerator;
    private final RabbitManager rabbitManager;

    public JWTResponse login(LoginDto loginDto) {
        UserDTO userDTO = rabbitManager.doLogin(loginDto);
        String token = jwtGenerator.generateJWT(userDTO.getName(),userDTO.getRoles());
        return new JWTResponse(token);
    }

    public UserDTO register(UserDTO userDTO) {
        userDTO.setRoles("ROLE_USER");
        return rabbitManager.doRegister(RabbitRequest.createRabbitRequest(userDTO));
    }

}
// add user to check correct roles