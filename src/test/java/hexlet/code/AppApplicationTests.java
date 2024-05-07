package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc

class AppApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JwtRequestPostProcessor token;

    private User testUser;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        testUser.setPasswordDigest(passwordEncoder.encode("12345678"));
        System.out.println("testUser before save -> " + testUser);
        userRepository.save(testUser);
        System.out.println("testUser after save -> " + testUser);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void someTest() {

    }

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/api/users").with(jwt())).andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        User data = Instancio.of(modelGenerator.getUserModel()).create();

        MockHttpServletRequestBuilder request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail(data.getEmail()).get();

        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(data.getFirstName());
        assertThat(user.getLastName()).isEqualTo(data.getLastName());
    }

    @Test
    void testUpdate() throws Exception {
        HashMap<String, String> data = new HashMap<>();
        data.put("firstName", "Mike");

        HashMap<String, String> logoPass = new HashMap<>();
        logoPass.put("username", testUser.getEmail());
        logoPass.put("password", "12345678");

        MockHttpServletRequestBuilder requestToken = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logoPass));

        String testToken = mockMvc.perform(requestToken).andReturn().getResponse().getContentAsString();

        MockHttpServletRequestBuilder request = put("/api/users/" + testUser.getId())
//                .with(token)
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));


        mockMvc.perform(request)
                .andExpect(status().isOk());

        User user = userRepository.findById(testUser.getId()).get();
        assertThat(user.getFirstName()).isEqualTo("Mike");
    }

    void filterTest() {

    }

}
