package hexlet.code.mapper;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.BaseEntity;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)

public class ReferenceMapper {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("statusToTaskStatus")
    public TaskStatus statusToTaskStatus(String status) {

        return taskStatusRepository.findTaskStatusBySlug(status)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("TaskStatus with slug = %s not found", status)));
    }


    @Named("insertLabelsIdToTask")
    public List<Label> insertLabelsIdToTask(List<Long> labelIds) {
        return labelRepository.findByIdIn(labelIds);
    }


    @Named("labelsToIds")
    public List<Long> labelsToIds(List<Label> labels) {
        List<Long> result = new ArrayList<>();
        for (Label label : labels) {
            Long id = label.getId();
            result.add(id);
        }
        return result;
    }
}
