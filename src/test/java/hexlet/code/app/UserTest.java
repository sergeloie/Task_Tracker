package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.utils.UserGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@Order(1)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGenerator userGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDTO userCreateDTO;

    private JwtRequestPostProcessor token;


    @BeforeEach
    public void setUp() {
        userCreateDTO = Instancio.of(userGenerator.getUserCreateDTOModel()).create();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }
}
