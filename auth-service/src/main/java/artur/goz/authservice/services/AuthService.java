package artur.goz.authservice.services;

import artur.goz.authservice.dto.JWTResponse;
import artur.goz.authservice.dto.LoginDto;
import artur.goz.authservice.dto.MyUserVO;
import artur.goz.authservice.dto.RegisterDto;
import artur.goz.authservice.rabbitmq.MessageSender;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService {

    @Autowired
    private final JWTGenerator jwtGenerator;

    @Autowired
    MessageSender messageSender;

    public JWTResponse login(LoginDto loginDto) {
        //перевірка логину

        MyUserVO myUserVO = messageSender.sendLogin(loginDto);
        String token = jwtGenerator.generateJWT(myUserVO.getName(),myUserVO.getRole());
        return new JWTResponse(token);
    }

    public MyUserVO register(RegisterDto registerDto) {
        MyUserVO myUserVO = messageSender.sendRegister(registerDto);
        return myUserVO;
    }
}
// add user to check correct roles