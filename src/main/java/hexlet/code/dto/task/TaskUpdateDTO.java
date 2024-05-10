package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private int index;

    @JsonProperty("assignee_id")
    private Long assigneeId;
    private String title;
    private String content;
    private String status;
    @JsonProperty("taskLabelIds")
    private List<Long> labels;
}
