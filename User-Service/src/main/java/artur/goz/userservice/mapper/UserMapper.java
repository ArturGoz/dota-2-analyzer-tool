package artur.goz.userservice.mapper;


import artur.goz.userservice.dto.MyUserDTO;
import artur.goz.userservice.models.MyUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    MyUser toUser(MyUserDTO any);
    MyUserDTO toUserDTO(MyUser any);
}
