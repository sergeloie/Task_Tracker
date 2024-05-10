package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.testrequest.RequestSender;
import hexlet.code.utils.JsonFieldExtractor;
import hexlet.code.utils.UserGenerator;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JsonFieldExtractor jsonFieldExtractor;

    @Autowired
    private UserGenerator userGenerator;

    @Autowired
    private RequestSender requestSender;
    @Autowired
    private UserRepository userRepository;


    @Test
    void testCreateAndShow() throws Exception {
        UserCreateDTO userCreateDTO = Instancio.of(userGenerator.getUserCreateDTOModel()).create();
        String result = requestSender.sendPostRequest("/api/users", userCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result).and(
                v -> v.node("id").isPresent(),
                v -> v.node("email").isEqualTo(userCreateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userCreateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userCreateDTO.getLastName()));

        Long id = jsonFieldExtractor.getFieldAsLong(result, "id");
        String show = requestSender.sendGetRequest("/api/users/" + id)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(show).and(
                v -> v.node("email").isEqualTo(userCreateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userCreateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userCreateDTO.getLastName()));
    }

    @Test
    void testIndex() throws Exception {
        String result = requestSender.sendGetRequest("/api/users")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result);
        assertThat(result).contains("hexlet@example.com");
    }

    @Test
    void testUpdate() throws Exception {
        UserCreateDTO userCreateDTO = Instancio.of(userGenerator.getUserCreateDTOModel()).create();
        String result = requestSender.sendPostRequest("/api/users", userCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result).and(
                v -> v.node("id").isPresent(),
                v -> v.node("email").isEqualTo(userCreateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userCreateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userCreateDTO.getLastName()));

        Long id = jsonFieldExtractor.getFieldAsLong(result, "id");

        UserUpdateDTO userUpdateDTO = Instancio.of(userGenerator.getUserUpdateDTOModel()).create();
        requestSender.sendPutRequest("/api/users/" + id, userUpdateDTO)
                .andExpect(status().isNotFound());

        HashMap<String, String> logoPass = new HashMap<>();
        logoPass.put("username", userCreateDTO.getUsername());
        logoPass.put("password", userCreateDTO.getPassword());

        String testToken = requestSender.sendPostRequest("/api/login", logoPass)
                .andReturn().getResponse().getContentAsString();

        MockHttpServletRequestBuilder request = put("/api/users/" + id)
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDTO));
        String updateResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(updateResult).and(
                v -> v.node("id").isPresent(),
                v -> v.node("id").isEqualTo(id),
                v -> v.node("email").isEqualTo(userUpdateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userUpdateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userUpdateDTO.getLastName()));
        UserDTO userFromRest = objectMapper.readValue(updateResult, UserDTO.class);
        UserDTO userFromDB = userMapper.map(userRepository.getReferenceById(id));
        assertThat(userFromRest).isEqualTo(userFromDB);
    }

    @Test
    void deleteTest() throws Exception {
        UserCreateDTO userCreateDTO = Instancio.of(userGenerator.getUserCreateDTOModel()).create();
        String result = requestSender.sendPostRequest("/api/users", userCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result).and(
                v -> v.node("id").isPresent(),
                v -> v.node("email").isEqualTo(userCreateDTO.getUsername()),
                v -> v.node("firstName").isEqualTo(userCreateDTO.getFirstName()),
                v -> v.node("lastName").isEqualTo(userCreateDTO.getLastName()));

        Long id = jsonFieldExtractor.getFieldAsLong(result, "id");

        requestSender.sendGetRequest("/api/users/" + id).andExpect(status().isOk());
        requestSender.sendDeleteRequest("/api/users/" + id).andExpect(status().isNotFound());
        requestSender.sendGetRequest("/api/users/" + id).andExpect(status().isOk());

        HashMap<String, String> logoPass = new HashMap<>();
        logoPass.put("username", userCreateDTO.getUsername());
        logoPass.put("password", userCreateDTO.getPassword());

        String testToken = requestSender.sendPostRequest("/api/login", logoPass)
                .andReturn().getResponse().getContentAsString();
        MockHttpServletRequestBuilder request = delete("/api/users/" + id)
                .header("Authorization", "Bearer " + testToken);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        requestSender.sendGetRequest("/api/users/" + id).andExpect(status().isNotFound());
    }
}
