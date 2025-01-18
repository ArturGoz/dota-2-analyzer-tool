package artur.goz.authservice.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RegisterDto {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}
