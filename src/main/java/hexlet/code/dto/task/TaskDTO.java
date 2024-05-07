package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private int index;
    private LocalDate createdAt;
    private String title;
    private String content;
    private String status;
    @JsonProperty("taskLabelIds")
    private List<Long> labels;

    @JsonProperty("assignee_id")
    private Long assigneeId;

}
