package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.utils.ModelGenerator;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtRequestPostProcessor token;

    private String createTaskDTOJSON;

    private TaskCreateDTO theTaskCreateDTO;

    @Autowired
    private Faker faker;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    private String getTaskCreateDTOJSON(TaskCreateDTO taskCreateDTO) throws Exception {
        return objectMapper.writeValueAsString(taskCreateDTO);
    }

    private TaskCreateDTO getTaskCreateDTO() throws Exception {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setIndex(faker.number().numberBetween(1, 100));
        taskCreateDTO.setAssigneeId(1);
        taskCreateDTO.setTitle(faker.text().text(20, 50));
        taskCreateDTO.setContent(faker.text().text(100, 200));
        taskCreateDTO.setStatus(getRandomSlug());
        return taskCreateDTO;
    }

    private String getRandomSlug() {
        List<String> list = taskStatusRepository.findAll().stream().map(TaskStatus::getSlug).toList();
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));

    }

    @Test
    void testCreate() throws Exception {
        theTaskCreateDTO = getTaskCreateDTO();
        createTaskDTOJSON = getTaskCreateDTOJSON(theTaskCreateDTO);
        mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType("application/json")
                        .content(createTaskDTOJSON))
                .andExpect(status().isCreated());
        String name = theTaskCreateDTO.getTitle();
        Task task = taskRepository.findByName(name);
        assertNotNull(task);

        long id = task.getId();
        String resultShow = mockMvc.perform(get("/api/tasks/" + id)
                .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(resultShow).contains(name);

        String resultIndex = mockMvc.perform(get("/api/tasks")
                .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(resultIndex).contains(name);


    }

    @Test
    void testUpdate() throws Exception {
        theTaskCreateDTO = getTaskCreateDTO();
        createTaskDTOJSON = getTaskCreateDTOJSON(theTaskCreateDTO);
        mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType("application/json")
                        .content(createTaskDTOJSON))
                .andExpect(status().isCreated());
        String name = theTaskCreateDTO.getTitle();
        Task task = taskRepository.findByName(name);
        assertNotNull(task);

        long id = task.getId();

        String resultShow = mockMvc.perform(get("/api/tasks/" + id)
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(resultShow).contains(name);

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setAssigneeId(1);
        taskUpdateDTO.setContent("updatedContent");
        taskUpdateDTO.setIndex(1001);
        taskUpdateDTO.setTitle("updatedTitle");
        String taskUpdateJSON = objectMapper.writeValueAsString(taskUpdateDTO);

        mockMvc.perform(put("/api/tasks/" + id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskUpdateJSON))
                .andExpect(status().isOk());

        String resultUpdate = mockMvc.perform(get("/api/tasks/" + id)
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(resultUpdate).contains("updatedContent");
        assertThat(resultUpdate).contains("updatedTitle");
        assertThat(resultUpdate).contains("1001");

    }



}
