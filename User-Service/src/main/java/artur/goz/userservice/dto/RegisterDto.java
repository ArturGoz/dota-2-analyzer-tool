package artur.goz.userservice.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RegisterDto {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 25, message = "Name must be between 5 and 25 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private String confirmPassword;
}
