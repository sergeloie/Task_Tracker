package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.testrequest.RequestSender;
import hexlet.code.utils.LabelGenerator;
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
public class LabelTest {

    @Autowired
    private RequestSender requestSender;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private LabelGenerator labelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testIndexLabelNew() throws Exception {
        String result = requestSender.sendGetRequest("/api/labels")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).contains("feature");
        assertThat(result).contains("bug");
    }

    @Test
    void testShowLabel() throws Exception {
        Long featureId = labelRepository.findByName("feature").getId();
        Long bugId = labelRepository.findByName("bug").getId();

        String featureResult = requestSender.sendGetRequest("/api/labels/" + featureId)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String bugResult = requestSender.sendGetRequest("/api/labels/" + bugId)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(featureResult).and(
                v -> v.node("id").isEqualTo(featureId),
                v -> v.node("name").isEqualTo("feature"));
        assertThatJson(bugResult).and(
                v -> v.node("id").isEqualTo(bugId),
                v -> v.node("name").isEqualTo("bug"));
    }

    @Test
    void testCreateLabelNew() throws Exception {
        LabelCreateDTO labelCreateDTO = Instancio.of(labelGenerator.getLabelCreateModel()).create();
        String resultOfCreation = requestSender.sendPostRequest("/api/labels", labelCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(resultOfCreation).and(
                v -> v.node("id").isPresent(),
                v -> v.node("name").isEqualTo(labelCreateDTO.getName()));

        JsonNode jsonNode = objectMapper.readTree(resultOfCreation);
        long id = jsonNode.get("id").asLong();

        LabelDTO labelCreated = objectMapper.readValue(resultOfCreation, LabelDTO.class);
        LabelDTO labelRested = objectMapper.readValue(requestSender.sendGetRequest("/api/labels/" + id)
                .andReturn().getResponse().getContentAsString(), LabelDTO.class);
        Label label =  labelRepository.getReferenceById(id);
        LabelDTO labelRepositored = labelMapper.map(label);

        assertThat(labelCreated).isNotNull();
        assertThat(labelRested).isNotNull();
        assertThat(labelRepositored).isNotNull();

        assertThat(labelCreated).isEqualTo(labelRested);
        assertThat(labelCreated).isEqualTo(labelRepositored);
    }

    @Test
    void testUpdateLabel() throws Exception {
        LabelCreateDTO labelCreateDTO = Instancio.of(labelGenerator.getLabelCreateModel()).create();

        String createResult = requestSender.sendPostRequest("/api/labels", labelCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        LabelDTO labelDTO = objectMapper.readValue(createResult, LabelDTO.class);
        Long labelId = labelDTO.getId();

        LabelUpdateDTO labelUpdateDTO = Instancio.of(labelGenerator.getLabelUpdateModel()).create();
        String updateResult = requestSender.sendPutRequest("/api/labels/" + labelId, labelUpdateDTO)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LabelDTO updatedLabelDTO = objectMapper.readValue(updateResult, LabelDTO.class);
        assertThat(labelUpdateDTO.getName()).isEqualTo(updatedLabelDTO.getName());
        LabelDTO labelDTOfromRepository = labelMapper.map(labelRepository.findByName(labelUpdateDTO.getName()));
        assertThat(updatedLabelDTO).isEqualTo(labelDTOfromRepository);
    }

    @Test
    void deleteTest() throws Exception {
        LabelCreateDTO labelCreateDTO = Instancio.of(labelGenerator.getLabelCreateModel()).create();

        String result = requestSender.sendPostRequest("/api/labels", labelCreateDTO)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        LabelDTO labelDTO = objectMapper.readValue(result, LabelDTO.class);
        Long labelId = labelDTO.getId();

        requestSender.sendGetRequest("/api/labels/" + labelId).andExpect(status().isOk());
        requestSender.sendDeleteRequest("/api/labels/" + labelId).andExpect(status().isNoContent());
        requestSender.sendGetRequest("/api/labels/" + labelId).andExpect(status().isNotFound());
    }
}
