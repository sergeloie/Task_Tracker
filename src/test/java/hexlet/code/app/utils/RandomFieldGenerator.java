package hexlet.code.app.utils;

import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class RandomFieldGenerator {

    private UserRepository userRepository;
    private LabelRepository labelRepository;
    private TaskStatusRepository taskStatusRepository;

    public Long getRandomAssigneeId() {
        long size = userRepository.count();
        return new Random().nextLong(1, size);
    }


    public Long getRandomLabelId() {
        long size = labelRepository.count();
        return new Random().nextLong(1, size);
    }

    public Long getRandomTaskStatusId() {
        long size = taskStatusRepository.count();
        return new Random().nextLong(1, size);
    }



}
