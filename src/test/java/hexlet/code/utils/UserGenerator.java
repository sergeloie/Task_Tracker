package hexlet.code.utils;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UserGenerator {

    private Model<User> userModel;
    private Model<UserCreateDTO> userCreateDTOModel;
    private Model<UserUpdateDTO> userUpdateDTOModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .toModel();

        userCreateDTOModel = Instancio.of(UserCreateDTO.class)
                .supply(Select.field(UserCreateDTO::getUsername), () -> faker.internet().emailAddress())
                .supply(Select.field(UserCreateDTO::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(UserCreateDTO::getLastName), () -> faker.name().lastName())
                .supply(Select.field(UserCreateDTO::getPassword), () -> faker.text().text(10, 20))
                .toModel();

        userUpdateDTOModel = Instancio.of(UserUpdateDTO.class)
                .supply(Select.field(UserUpdateDTO::getUsername), () -> faker.internet().emailAddress())
                .supply(Select.field(UserUpdateDTO::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(UserUpdateDTO::getLastName), () -> faker.name().lastName())
                .supply(Select.field(UserUpdateDTO::getPassword), () -> faker.text().text(10, 20))
                .toModel();
    }
}
