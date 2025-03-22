
package artur.goz.userservice.controlles;

import artur.goz.userservice.models.User;
import artur.goz.userservice.services.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    @Autowired
    UserServiceImpl myUserService;

/*    @GetMapping("/getUserInfo")
    public ResponseEntity<MyUserVO> getUserInfo2(
            @RequestHeader(value = "X-User-Name") String username) {

        MyUserVO myUserVO = myUserService.findUserVO(username);
        myUserVO.setPassword(null);
        return ResponseEntity.ok(myUserVO);
    }


    @PatchMapping("/passwordChange")
    public ResponseEntity<String> getPassword(@RequestBody ChangePassword changePassword,
                              @RequestHeader(value = "X-User-Name", required = false) String username){

        User user = myUserService.getMyUserByName(username)
                .orElseThrow(() -> new RuntimeException("User not found for changing password"));


        if (changePassword.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Новий пароль має бути щонайменше 8 символів!");
        }

        String encodedNewPassword = myUserService.encodePassword(changePassword.getNewPassword());

        log.info("New password: {}", encodedNewPassword);
        myUserService.updatePassword(user, encodedNewPassword);
        return ResponseEntity.ok("Пароль був змінений успішно");
    }*/
}
