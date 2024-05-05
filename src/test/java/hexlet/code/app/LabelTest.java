package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.controller.LabelController;
import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.utils.LabelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelTest {

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private LabelGenerator labelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelController labelController;

    @Autowired
    private ObjectMapper objectMapper;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @Autowired
    private MockMvc mockMvc;



    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    void testIndexLabel() throws Exception {
        MockHttpServletRequestBuilder indexRequest = get("/api/labels")
                .with(token);
        String result = mockMvc.perform(indexRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).contains("feature");
        assertThat(result).contains("bug");
    }

    @Test
    void testShowLabel() throws Exception {
        MockHttpServletRequestBuilder showRequest1 = get("/api/labels/1")
                .with(token);
        MockHttpServletRequestBuilder showRequest2 = get("/api/labels/2")
                .with(token);
        String result1 = mockMvc.perform(showRequest1)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String result2 = mockMvc.perform(showRequest2)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result1).contains("feature");
        assertThat(result2).contains("bug");
    }

    @Test
    void testCreateLabel() throws Exception {
        LabelCreateDTO testLabel = Instancio.of(labelGenerator.getLabelCreateModel()).create();
        MockHttpServletRequestBuilder createLabelRequest = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLabel));
        String result = mockMvc.perform(createLabelRequest)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        LabelDTO labelDTO = objectMapper.readValue(result, LabelDTO.class);
        assertThat(labelDTO.getCreatedAt()).isToday();
        assertThat(labelDTO.getName()).isEqualTo(testLabel.getName());

        Long labelId = labelDTO.getId();
        MockHttpServletRequestBuilder createdLabelRequest = get("/api/labels/" + labelId)
                .with(token);
        String result2 = mockMvc.perform(createdLabelRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LabelDTO createdLabelDTO = objectMapper.readValue(result2, LabelDTO.class);

        assertThat(labelDTO).isEqualTo(createdLabelDTO);
    }

    @Test
    void testUpdateLabel() throws Exception {
        LabelCreateDTO labelCreateDTO = Instancio.of(labelGenerator.getLabelCreateModel()).create();
        MockHttpServletRequestBuilder createLabelRequest = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelCreateDTO));
        String result = mockMvc.perform(createLabelRequest)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        LabelDTO labelDTO = objectMapper.readValue(result, LabelDTO.class);
        Long labelId = labelDTO.getId();

        LabelUpdateDTO labelUpdateDTO = Instancio.of(labelGenerator.getLabelUpdateModel()).create();
        MockHttpServletRequestBuilder updateLabelRequest = put("/api/labels/" + labelId)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelUpdateDTO));
        String result2 = mockMvc.perform(updateLabelRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LabelDTO labelDTO2 = objectMapper.readValue(result2, LabelDTO.class);
        assertThat(labelUpdateDTO.getName()).isEqualTo(labelDTO2.getName());
        LabelDTO labelDTOfromRepository = labelMapper.map(labelRepository.findByName(labelUpdateDTO.getName()));
        assertThat(labelDTO2).isEqualTo(labelDTOfromRepository);
    }

    @Test
    void deleteTest() throws Exception {
        LabelCreateDTO labelCreateDTO = Instancio.of(labelGenerator.getLabelCreateModel()).create();
        MockHttpServletRequestBuilder createLabelRequest = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelCreateDTO));
        String result = mockMvc.perform(createLabelRequest)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        LabelDTO labelDTO = objectMapper.readValue(result, LabelDTO.class);
        Long labelId = labelDTO.getId();
        mockMvc.perform(get("/api/labels/" + labelId)).andExpect(status().isOk());
        MockHttpServletRequestBuilder deleteUnassigned = delete("/api/labels/" + labelId)
                .with(token);
        mockMvc.perform(deleteUnassigned).andExpect(status().isOk());
        mockMvc.perform(get("/api/labels/" + labelId)).andExpect(status().isNotFound());
    }
}
