package hexlet.code;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.testrequest.RequestSender;
import hexlet.code.utils.JsonFieldExtractor;
import hexlet.code.utils.TaskStatusGenerator;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskStatusTest {

    @Autowired
    private JsonFieldExtractor jsonFieldExtractor;

    @Autowired
    private RequestSender requestSender;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusGenerator taskStatusGenerator;

    @Test
    void testCreate() throws Exception {
        TaskStatusCreateDTO taskStatusCreateDTO = Instancio.of(taskStatusGenerator.getTaskStatusCreateDTOModel())
                .create();
        String createResult = requestSender.sendPostRequest("/api/task_statuses", taskStatusCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(createResult).and(
                v -> v.node("id").isPresent(),
                v -> v.node("name").isEqualTo(taskStatusCreateDTO.getName()),
                v -> v.node("slug").isEqualTo(taskStatusCreateDTO.getSlug()));
        Long id = jsonFieldExtractor.getFieldAsLong(createResult, "id");
        String name = jsonFieldExtractor.getFieldAsString(createResult, "name");
        TaskStatus taskStatus = taskStatusRepository.getReferenceById(id);
        assertThat(taskStatus.getName()).isEqualTo(name);
    }


    @Test
    void testIndex() throws Exception {
        String result = requestSender.sendGetRequest("/api/task_statuses")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result);
        assertThat(result).contains("Draft");
    }


    @Test
    void testShow() throws Exception {
        Long id = taskStatusRepository.findTaskStatusBySlug("to_review")
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with slug 'to_review' not found")).getId();
        String result = requestSender.sendGetRequest("/api/task_statuses/" + id)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(result).and(
                v -> v.node("id").isEqualTo(id),
                v -> v.node("slug").isEqualTo("to_review"),
                v -> v.node("name").isEqualTo("ToReview"));
    }


    @Test
    void testUpdate() throws Exception {
        TaskStatusCreateDTO taskStatusCreateDTO = Instancio.of(taskStatusGenerator.getTaskStatusCreateDTOModel())
                .create();
        String createResult = requestSender.sendPostRequest("/api/task_statuses", taskStatusCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long id = jsonFieldExtractor.getFieldAsLong(createResult, "id");

        TaskStatusUpdateDTO taskStatusUpdateDTO = Instancio.of(taskStatusGenerator.getTaskStatusUpdateDTOModel())
                .create();
        String updateResult = requestSender.sendPutRequest(
                "/api/task_statuses/" + id, taskStatusUpdateDTO)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(updateResult).and(
                v -> v.node("name").isEqualTo(taskStatusUpdateDTO.getName()),
                v -> v.node("slug").isEqualTo(taskStatusUpdateDTO.getSlug()));
        String slug = jsonFieldExtractor.getFieldAsString(updateResult, "slug");
        TaskStatus statusBySlug = taskStatusRepository.findTaskStatusBySlug(slug).get();
        TaskStatus statusById = taskStatusRepository.getReferenceById(id);
        assertThat(statusBySlug).isEqualTo(statusById);
    }


    @Test
    void deleteTest() throws Exception {
        TaskStatusCreateDTO taskStatusCreateDTO = Instancio.of(taskStatusGenerator.getTaskStatusCreateDTOModel())
                .create();
        String createResult = requestSender.sendPostRequest("/api/task_statuses", taskStatusCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long id = jsonFieldExtractor.getFieldAsLong(createResult, "id");

        requestSender.sendGetRequest("/api/task_statuses/" + id).andExpect(status().isOk());
        requestSender.sendDeleteRequest("/api/task_statuses/" + id).andExpect(status().isNoContent());
        requestSender.sendGetRequest("/api/task_statuses/" + id).andExpect(status().isNotFound());

    }
}
