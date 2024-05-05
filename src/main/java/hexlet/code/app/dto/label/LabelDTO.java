package hexlet.code.app.dto.label;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class LabelDTO {
    private Long id;
    private String name;
    private LocalDate createdAt;
}
