package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

//@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder encoder;

    @Mapping(target = "passwordDigest", source = "password")
    @Mapping(target = "email", source = "username")
    public abstract User map(UserCreateDTO userCreateDTO);

    @Mapping(target = "username", source = "email")
    public abstract UserDTO map(User user);
    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(encoder.encode(password));
    }
}
