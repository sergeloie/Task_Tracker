package hexlet.code.utils;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.model.Label;
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
public class LabelGenerator {
    private Model<Label> labelModel;
    private Model<LabelCreateDTO> labelCreateModel;
    private Model<LabelUpdateDTO> labelUpdateModel;


    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .supply(Select.field(Label::getName), () -> faker.verb().base())
                .toModel();

        labelCreateModel = Instancio.of(LabelCreateDTO.class)
                .supply(Select.field(LabelCreateDTO::getName), () -> faker.animal().name())
                .toModel();

        labelUpdateModel = Instancio.of(LabelUpdateDTO.class)
                .supply(Select.field(LabelUpdateDTO::getName), () -> faker.artist().name())
                .toModel();
    }
}
