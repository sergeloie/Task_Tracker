package hexlet.code.component;

import hexlet.code.exception.BaseExceptionHandler;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import io.sentry.Sentry;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Component


@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class LabelInitializer implements ApplicationRunner {

    private final LabelRepository labelRepository;


    @Override
    public void run(ApplicationArguments args) throws DataIntegrityViolationException {
        Label feature = new Label();
        feature.setName("feature");

        Label bug = new Label();
        bug.setName("bug");

//        labelRepository.save(feature);
//        labelRepository.save(bug);

    }
}
