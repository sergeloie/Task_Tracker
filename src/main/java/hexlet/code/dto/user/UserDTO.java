package hexlet.code.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @JsonProperty("email")
    private String username;
    private LocalDate createdAt;
}
