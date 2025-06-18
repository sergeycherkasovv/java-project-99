package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
      uses = {JsonNullableMapper.class, ReferenceMapper.class} ,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      componentModel = MappingConstants.ComponentModel.SPRING,
      unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder encoder;

    public abstract UserDTO map(User model);

    @Mapping(source = "password", target = "passwordDigest")
    public abstract User map(UserCreateDTO dto);

    @Mapping(source = "password", target = "passwordDigest")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    public abstract User map(UserDTO dto);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(encoder.encode(password));
    }
}
