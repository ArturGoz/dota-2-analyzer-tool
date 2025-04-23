package artur.goz.userservice.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ChangePassword {
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Your password must contain upper and lower case letters and numbers, " +
                    "at least 7 and maximum 30 characters." +
                    "Password cannot contains spaces")
    private String newPassword;
}
