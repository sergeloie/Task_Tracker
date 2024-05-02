package hexlet.code.app.dto.task;

import java.time.LocalDate;

public class TaskDTO {
    private long id;
    private int index;
    private LocalDate createdAt;
    private String title;
    private String content;
    private String status;
    private long assigneeId;

}
