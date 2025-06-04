package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;

@Mapper(
      uses = {JsonNullableMapper.class} ,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      componentModel = MappingConstants.ComponentModel.SPRING,
      unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public abstract UserDTO map(User model);

    public abstract User map(UserCreateDTO dto);

    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    public abstract User map(UserDTO dto);

}
