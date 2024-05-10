package hexlet.code.utils;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TaskGenerator {

    private Model<Task> taskModel;
    private Model<TaskCreateDTO> taskCreateDTOModel;
    private Model<TaskUpdateDTO> taskUpdateDTOModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {

        taskCreateDTOModel = Instancio.of(TaskCreateDTO.class)
                .supply(Select.field(TaskCreateDTO::getIndex), () -> faker.random().nextInt(10000, 100000))
                .supply(Select.field(TaskCreateDTO::getTitle), () -> faker.coin())
                .supply(Select.field(TaskCreateDTO::getContent), () -> faker.lorem().sentence())
                .toModel();

    }
}
