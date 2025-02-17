package artur.goz.userservice.dto;

import artur.goz.userservice.models.MyUser;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class MyUserVO {
    private String name;
    private String email;
    private String password;
    private String role;
}

