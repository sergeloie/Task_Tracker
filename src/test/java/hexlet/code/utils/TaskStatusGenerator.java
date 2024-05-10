package hexlet.code.utils;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
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
public class TaskStatusGenerator {

    private Model<TaskStatus> taskStatusModel;
    private Model<TaskStatusCreateDTO> taskStatusCreateDTOModel;
    private Model<TaskStatusUpdateDTO> taskStatusUpdateDTOModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.name().firstName())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.name().lastName())
                .toModel();

        taskStatusCreateDTOModel = Instancio.of(TaskStatusCreateDTO.class)
                .supply(Select.field(TaskStatusCreateDTO::getName), () -> faker.text().text(10))
                .supply(Select.field(TaskStatusCreateDTO::getSlug), () -> faker.text().text(8))
                .toModel();

        taskStatusUpdateDTOModel = Instancio.of(TaskStatusUpdateDTO.class)
                .supply(Select.field(TaskStatusUpdateDTO::getName), () -> faker.text().text(10))
                .supply(Select.field(TaskStatusUpdateDTO::getSlug), () -> faker.text().text(8))
                .toModel();
    }

}
