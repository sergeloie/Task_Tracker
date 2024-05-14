package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import io.sentry.Sentry;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class TaskStatusInitializer implements ApplicationRunner {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public void run(ApplicationArguments args) {
        TaskStatus draft = new TaskStatus();
        draft.setName("Draft");
        draft.setSlug("draft");

        TaskStatus toReview = new TaskStatus();
        toReview.setName("ToReview");
        toReview.setSlug("to_review");

        TaskStatus toBeFixed = new TaskStatus();
        toBeFixed.setName("ToBeFixed");
        toBeFixed.setSlug("to_be_fixed");

        TaskStatus toPublish = new TaskStatus();
        toPublish.setName("ToPublish");
        toPublish.setSlug("to_publish");

        TaskStatus published = new TaskStatus();
        published.setName("Published");
        published.setSlug("published");

        List<TaskStatus> taskStatuses = List.of(draft, toReview, toBeFixed, toPublish, published);
        for (TaskStatus taskStatus : taskStatuses) {
            try {
                taskStatusRepository.save(taskStatus);
            } catch (DataIntegrityViolationException e) {
                Sentry.captureException(e);
            }
        }
    }
}
