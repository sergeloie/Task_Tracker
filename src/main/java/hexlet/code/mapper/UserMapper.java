package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
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

public abstract class UserMapper {


    @Autowired
    private PasswordEncoder encoder;

    @Mapping(target = "passwordDigest", source = "password")
    @Mapping(target = "email", source = "username")
    public abstract User map(UserCreateDTO userCreateDTO);

    @Mapping(target = "username", source = "email")
    public abstract UserDTO map(User user);

    @Mapping(target = "email", source = "username")
    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO userCreateDTO) {
        String password = userCreateDTO.getPassword();
        userCreateDTO.setPassword(encoder.encode(password));
    }

    @BeforeMapping
    public void encryptPassword(UserUpdateDTO userUpdateDTO) {
        String password = userUpdateDTO.getPassword();
        if (password != null) {
            userUpdateDTO.setPassword(encoder.encode(password));
        }
    }
}
