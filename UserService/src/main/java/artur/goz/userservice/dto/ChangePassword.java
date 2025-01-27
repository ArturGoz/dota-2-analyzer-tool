package artur.goz.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePassword {
    String oldPassword;
    String newPassword;
}
