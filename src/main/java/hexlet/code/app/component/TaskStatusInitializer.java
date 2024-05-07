package hexlet.code.app.component;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

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

        try {
            taskStatusRepository.save(draft);
        } catch (DataIntegrityViolationException ignored) {

        }

        try {
            taskStatusRepository.save(toReview);
        } catch (DataIntegrityViolationException ignored) {

        }

        try {
            taskStatusRepository.save(toBeFixed);
        } catch (DataIntegrityViolationException ignored) {

        }

        try {
            taskStatusRepository.save(toPublish);
        } catch (DataIntegrityViolationException ignored) {

        }

        try {
            taskStatusRepository.save(published);
        } catch (DataIntegrityViolationException ignored) {

        }

    }
}
