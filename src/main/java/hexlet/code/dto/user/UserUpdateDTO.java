package hexlet.code.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    @Email
    @JsonProperty("email")
    private String username;
    @Size(min = 3)
    private String password;
}
