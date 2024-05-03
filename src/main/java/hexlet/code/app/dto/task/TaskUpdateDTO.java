package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {
    private int index;
    private long assigneeId;
    private String title;
    private String content;
    private String status;
}
