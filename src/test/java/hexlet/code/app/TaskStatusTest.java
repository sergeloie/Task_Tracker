package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.utils.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtRequestPostProcessor token;

    private String authtoken;

    private TaskStatus testTaskStatus;

    @Autowired
    private Faker faker;

    @BeforeEach
    public void setUp() throws Exception {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "hexlet@example.com");
        credentials.put("password", "qwerty");
        MockHttpServletRequestBuilder request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials));
        authtoken = mockMvc.perform(request)
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    void testShow() throws Exception {
        MockHttpServletRequestBuilder showRequest = get("/api/task_statuses")
                .with(token);
//                .header("Authorization", "Bearer " + authtoken);
        String result = mockMvc.perform(showRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).contains("Draft");

    }

    @Test
    void testIndex() throws Exception {
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);
        long id = testTaskStatus.getId();
        MockHttpServletRequestBuilder indexRequest = get("/api/task_statuses/" + id)
                .with(token);
//                .header("Authorization", "Bearer " + authtoken);
        String result = mockMvc.perform(indexRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).contains(testTaskStatus.getName());
        assertThat(result).contains(testTaskStatus.getSlug());
    }

    @Test
    void testCreate() throws Exception {
        Map<String, String> map = new HashMap<>();
        String name = faker.text().text(20, 30);
        String slug = faker.text().text(20, 30);
        map.put("name", name);
        map.put("slug", slug);

        MockHttpServletRequestBuilder createRequest = post("/api/task_statuses")
                .with(token)
//                .header("Authorization", "Bearer " + authtoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map));
        mockMvc.perform(createRequest)
                .andExpect(status().isCreated());
        TaskStatus status = taskStatusRepository.findTaskStatusBySlug(slug).get();
        assertThat(status.getName()).isEqualTo(name);
    }

    @Test
    void testUpdate() throws Exception {
        Map<String, String> mapCreate = new HashMap<>();
        String name = faker.text().text(20, 30);
        String slug = faker.text().text(20, 30);
        mapCreate.put("name", name);
        mapCreate.put("slug", slug);

        MockHttpServletRequestBuilder createRequest = post("/api/task_statuses")
                .with(token)
//                .header("Authorization", "Bearer " + authtoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapCreate));
        mockMvc.perform(createRequest)
                .andExpect(status().isCreated());

        TaskStatus status = taskStatusRepository.findTaskStatusBySlug(slug).get();
        long id = status.getId();
        Map<String, String> updateMap = new HashMap<>();
        String name2 = faker.text().text(20, 30);
        String slug2 = faker.text().text(20, 30);
        updateMap.put("name", name2);
        updateMap.put("slug", slug2);

        MockHttpServletRequestBuilder updateRequest = put("/api/task_statuses/" + id)
                .with(token)
//                .header("Authorization", "Bearer " + authtoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMap));
        mockMvc.perform(updateRequest)
                .andExpect(status().isOk());

        TaskStatus updatedStatus = taskStatusRepository.findById(id).get();
        assertThat(updatedStatus.getName()).isEqualTo(name2);
        assertThat(updatedStatus.getSlug()).isEqualTo(slug2);
    }

    @Test
    void deleteTest() throws Exception {
        Map<String, String> mapCreate = new HashMap<>();
        String name = faker.text().text(20, 30);
        String slug = faker.text().text(20, 30);
        mapCreate.put("name", name);
        mapCreate.put("slug", slug);

        MockHttpServletRequestBuilder createRequest = post("/api/task_statuses")
                .with(token)
//                .header("Authorization", "Bearer " + authtoken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapCreate));
        mockMvc.perform(createRequest)
                .andExpect(status().isCreated());

        TaskStatus status = taskStatusRepository.findTaskStatusBySlug(slug).get();
        long id = status.getId();

        MockHttpServletRequestBuilder deleteRequest = delete("/api/task_statuses/" + id)
                .with(token);
//                .header("Authorization", "Bearer " + authtoken);
        mockMvc.perform(deleteRequest);

        MockHttpServletRequestBuilder indexRequest = get("/api/task_statuses/" + id)
                .with(token);
//                .header("Authorization", "Bearer " + authtoken);
        mockMvc.perform(indexRequest)
                .andExpect(status().isNotFound());

    }
}
