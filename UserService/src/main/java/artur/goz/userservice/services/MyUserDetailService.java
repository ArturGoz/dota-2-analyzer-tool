/*
package artur.goz.userservice.services;


import artur.goz.userservice.models.MyUser;
import artur.goz.userservice.models.MyUserDetails;
import artur.goz.userservice.repositories.MyUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private MyUserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepo.findByName(username);
        return user.map(MyUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException(username + " not found"));
    }
}
*/
