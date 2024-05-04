package hexlet.code.app.component;

import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class LabelInitializer implements ApplicationRunner {

    private LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Label feature = new Label();
        feature.setName("feature");

        Label bug = new Label();
        bug.setName("bug");

        try {
            labelRepository.save(feature);
        } catch (DataIntegrityViolationException ignored) {}

        try {
            labelRepository.save(bug);
        } catch (DataIntegrityViolationException ignored) {}

    }
}
