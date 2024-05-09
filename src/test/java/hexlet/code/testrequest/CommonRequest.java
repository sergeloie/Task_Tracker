package hexlet.code.testrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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

@Component
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class CommonRequest {

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    public ResultActions indexRequest(String path) throws Exception {
        return mockMvc.perform(get(path).with(jwt()));
    }

    public ResultActions showRequest(String path) throws Exception {
        return mockMvc.perform(get(path).with(jwt()));
    }

    public <T> ResultActions createRequest(String path, T entity) throws Exception {
        return mockMvc.perform(post(path).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)));
    }

    public <T> ResultActions updateRequest(String path, T entity) throws Exception {
        return mockMvc.perform(put(path).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)));
    }

    public ResultActions deleteRequest(String path) throws Exception {
        return mockMvc.perform(delete(path).with(jwt()));
    }

}
