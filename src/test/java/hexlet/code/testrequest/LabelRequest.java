package hexlet.code.testrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class LabelRequest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;





    public ResultActions saveLabelRequest(LabelCreateDTO labelCreateDTO) throws Exception {
        return mockMvc.perform(post("/api/labels").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelCreateDTO)));
    }

    public ResultActions getLabelsRequest() throws Exception {
        return mockMvc.perform(get("/api/labels").with(jwt()));
    }

    public ResultActions getLabelByIdRequest(long labelId) throws Exception {
        return mockMvc.perform(get("/api/labels/" + labelId).with(jwt()));
    }

    public ResultActions updateLabelRequest(LabelUpdateDTO labelUpdateDTO, long labelId) throws Exception {
        return mockMvc.perform(put("api/labels/" + labelId).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(labelUpdateDTO)));
    }

    public ResultActions deleteLabelRequest(long labelId) throws Exception {
        return mockMvc.perform(delete("api/labels/" + labelId).with(jwt()));
    }




}
