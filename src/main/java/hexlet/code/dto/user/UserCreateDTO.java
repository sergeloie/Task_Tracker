package hexlet.code.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @Email
    @NotNull
    @JsonProperty("email")
    private String username;
    private String firstName;
    private String lastName;
    @NotNull
    @Size(min = 3)
    private String password;
}
