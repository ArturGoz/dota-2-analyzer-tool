package artur.goz.userservice.services;

import artur.goz.userservice.dto.MyUserDTO;
import artur.goz.userservice.dto.MyUserVO;
import artur.goz.userservice.dto.RegisterDto;
import artur.goz.userservice.exception.EntityAlreadyExistsException;
import artur.goz.userservice.exception.StatusCode;
import artur.goz.userservice.mapper.UserMapper;
import artur.goz.userservice.models.MyUser;
import artur.goz.userservice.repositories.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MyUserServiceImpl {

    private final MyUserRepo myUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void addRoleToUsername(String name, String role) throws Exception {
        Optional<MyUser> myUser = myUserRepo.findByName(name);
        if(myUser.isEmpty()){
            throw new Exception("no such user");
        }
        myUser.get().getRoles().add(role);
        myUserRepo.save(myUser.get());
    }

    public Boolean checkUser(String username) {
        return myUserRepo.existsByName(username);
    }

    public void registerUser(MyUser myUser){
        MyUser myUserNew = new MyUser();

        myUserNew.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUserNew.setName(myUser.getName());
        myUserNew.setRoles(Set.of("ROLE_USER"));
        myUserNew.setEmail(myUser.getEmail());

        myUserRepo.save(myUserNew);
    }

    public MyUserDTO registerUser(RegisterDto registerDto){

        if(!checkUser(registerDto.getName())){
            throw new EntityAlreadyExistsException("User already exists", StatusCode.DUPLICATE_USERNAME.name());
        }

        MyUser myUserNew = new MyUser();

        myUserNew.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        myUserNew.setName(registerDto.getName());
        myUserNew.setRoles(Set.of("ROLE_USER"));
        myUserNew.setEmail(registerDto.getEmail());

       MyUser newUser = myUserRepo.save(myUserNew);
       return userMapper.toUserDTO(newUser);
    }

    public void registerAdmin(){
        MyUser myUserNew = new MyUser();

        myUserNew.setPassword(passwordEncoder.encode("ADMIN123"));
        myUserNew.setName("ADMIN123");
        myUserNew.setRoles(Set.of("ROLE_USER","ROLE_ADMIN"));
        myUserNew.setEmail("admin@gmail.com");
        myUserNew.setMonthlyLimit(Integer.MAX_VALUE);

        myUserRepo.save(myUserNew);
    }

    public Optional<MyUser> getMyUserByName(String username){
        return myUserRepo.findByName(username);
    }

    public void updatePassword(MyUser myUser, String newPassword){
        myUser.setPassword(newPassword);
        myUserRepo.save(myUser);
    }

    public void decrementUserLimit(String username){
        MyUser myUser= getMyUserByName(username).orElseThrow(() -> new RuntimeException("no such user"));
        myUser.setMonthlyLimit(myUser.getMonthlyLimit() - 1);
        myUserRepo.save(myUser);
    }

    public MyUserVO createMyUserVO(MyUser myUser){

        MyUserVO myUserVO = new MyUserVO();
        myUserVO.setEmail(myUser.getEmail());
        myUserVO.setName(myUser.getName());
        myUserVO.setPassword(myUser.getPassword());
        myUserVO.setRole(String.join(",", myUser.getRoles()));

        return myUserVO;
    }

    public MyUserVO auth(String username, String password){
        MyUser myUser = getMyUserByName(username).orElseThrow(() -> new RuntimeException("no such user"));
        if(!doPasswordMatch(myUser, password)) {
            throw new RuntimeException("wrong password");
        }
        return createMyUserVO(myUser);
    }

    public boolean doPasswordMatch(MyUser myUser, String password){
        return passwordEncoder.matches(password, myUser.getPassword());
    }

    public MyUserVO findUserVO(String username) {
        MyUser myUser = getMyUserByName(username).orElseThrow();
        return createMyUserVO(myUser);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
