package hexlet.code.app.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {
    private int index;

    @JsonProperty("assignee_id")
    private long assigneeId;
    private String title;
    private String content;
    private String status;
}
