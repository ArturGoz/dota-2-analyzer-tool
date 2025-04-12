package artur.goz.userservice.mapper;


import artur.goz.userservice.dto.UserDTO;
import artur.goz.userservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toUser(UserDTO any);
    UserDTO toUserDTO(User any);
}
