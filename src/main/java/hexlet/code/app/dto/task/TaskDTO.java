package hexlet.code.app.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private long id;
    private int index;
    private LocalDate createdAt;
    private String title;
    private String content;
    private String status;

    @JsonProperty("assignee_id")
    private long assigneeId;

}
