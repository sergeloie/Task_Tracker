package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.testrequest.CommonRequest;
import hexlet.code.utils.UserGenerator;
import hexlet.code.utils.JsonFieldExtractor;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTest {

    @Autowired
    private JsonFieldExtractor jsonFieldExtractor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGenerator userGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDTO userCreateDTO;

    private JwtRequestPostProcessor token;
    @Autowired
    private CommonRequest commonRequest;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateAndShow() throws Exception {
        UserCreateDTO userCreateDTO = Instancio.of(userGenerator.getUserCreateDTOModel()).create();
        String result = commonRequest.createRequest("/api/users", userCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result).and(
                v -> v.node("id").isPresent(),
                v-> v.node("email").isEqualTo(userCreateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userCreateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userCreateDTO.getLastName()));

        Long id = jsonFieldExtractor.getFieldAsLong(result, "id");
        String show = commonRequest.showRequest("/api/users/" + id)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(show).and(
                v-> v.node("email").isEqualTo(userCreateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userCreateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userCreateDTO.getLastName()));
    }

    @Test
    void testIndex() throws Exception {
        Long id = userRepository.findByEmail("hexlet@example.com").get().getId();
        String result = commonRequest.indexRequest("/api/users/" + id)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result).and(
                v -> v.node("id").isPresent(),
                v-> v.node("email").isEqualTo("hexlet@example.com"));
    }
}
