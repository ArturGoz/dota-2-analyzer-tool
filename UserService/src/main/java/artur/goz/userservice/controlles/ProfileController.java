/*
package artur.goz.userservice.controlles;


import artur.goz.userservice.models.MyUser;
import artur.goz.userservice.models.MyUserDetails;
import artur.goz.userservice.services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class ProfileController {
    @Autowired
    MyUserService myUserService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

*/
/*    @GetMapping("/general")
    public String getGeneral(@RequestHeader("Authorization") String token){
        if(user == null)
            return "login";

        Optional<MyUser> optionalUser = myUserService.getMyUserByName(user.getUsername());
        if (optionalUser.isPresent()) {
            model.addAttribute("userName", optionalUser.get().getName());
            model.addAttribute("userEmail", optionalUser.get().getEmail());
        }
        return "profile";
    }*//*


    @GetMapping("/password")
    public String getPasswordPage(@AuthenticationPrincipal MyUserDetails user){
        if (user == null) {
            return "login";
        }
        return "password";
    }

    @PatchMapping("/passwordChange")
    public String getPassword(@AuthenticationPrincipal MyUserDetails user,
                              @RequestParam String oldPassword,
                              @RequestParam String newPassword,
                              Model model){
        System.out.println(oldPassword + newPassword);
        if (user == null) {
            return "login";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("error", "Старий пароль введено неправильно!");
            return "password";
        }

        if (newPassword.length() < 8) {
            model.addAttribute("error", "Новий пароль має бути щонайменше 8 символів!");
            return "password";
        }

        // Оновлення пароля
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        Optional<MyUser> optionalUser = myUserService.getMyUserByName(user.getUsername());

        if (optionalUser.isPresent()) {
            MyUser myUser = optionalUser.get();
            myUserService.updatePassword(myUser, encodedNewPassword);
        }
        return "redirect:/api/login?logout";
    }

}*/
