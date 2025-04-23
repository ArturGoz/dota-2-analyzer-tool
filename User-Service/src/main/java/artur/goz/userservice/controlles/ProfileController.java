
package artur.goz.userservice.controlles;

import artur.goz.userservice.dto.RemoteResponse;
import artur.goz.userservice.dto.UserDTO;
import artur.goz.userservice.dto.ChangePassword;
import artur.goz.userservice.exception.EntityNotFoundException;
import artur.goz.userservice.exception.StatusCode;
import artur.goz.userservice.models.User;
import artur.goz.userservice.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/profile")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    private final UserServiceImpl myUserService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public ResponseEntity<RemoteResponse> profileInfo(
            @RequestHeader(value = "X-User-Name") String username) {

        UserDTO myUserDTO = myUserService.getUserByName(username);
        return ResponseEntity.ok(RemoteResponse.create(true,
                "User was successfully obtained", List.of(myUserDTO)));
    }


    @PatchMapping("/changePassword")
    public ResponseEntity<RemoteResponse> changePassword(@RequestBody @Valid ChangePassword changePassword,
                              @RequestHeader(value = "X-User-Name", required = false) String username){

        UserDTO myUserDTO = myUserService.getUserByName(username);

        if (!passwordEncoder.matches(changePassword.getOldPassword(),myUserDTO.getPassword())) {
            throw new EntityNotFoundException("Wrong password",
                    StatusCode.WRONG_PASSWORD.name()
            );
        }

        String encodedNewPassword = passwordEncoder.encode(changePassword.getNewPassword());
        myUserDTO.setPassword(encodedNewPassword);
        myUserService.updateUser(username, myUserDTO);
        return ResponseEntity.ok(RemoteResponse.create(true,
                "Password was successfully changed", List.of(encodedNewPassword)));
    }
}
