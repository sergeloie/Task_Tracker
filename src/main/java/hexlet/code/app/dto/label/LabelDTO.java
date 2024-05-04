package hexlet.code.app.dto.label;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LabelDTO {
    private long id;
    private String name;
    private LocalDate createdAt;
}
