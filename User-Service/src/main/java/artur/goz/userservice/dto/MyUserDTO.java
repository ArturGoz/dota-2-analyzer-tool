package artur.goz.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyUserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
}
