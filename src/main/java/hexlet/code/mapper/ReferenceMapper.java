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
//        return entityManager.createQuery("SELECT ts FROM TaskStatus ts WHERE ts.slug = :status", TaskStatus.class)
//                .setParameter("status", status)
//                .getSingleResult();
        return taskStatusRepository.findTaskStatusBySlug(status)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("TaskStatus with slug = %s not found", status)));
    }

//    @Named("insertLabelsToTask")
//    public List<Label> insertLabelsToTask(List<String> names) {
//        if (names == null || names.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        List<Label> labels = new ArrayList<>();
//        for (String name : names) {
//            Label label = entityManager.createQuery("SELECT l FROM Label l WHERE l.name = :name", Label.class)
//                    .setParameter("name", name)
//                    .getSingleResult();
//            labels.add(label);
//        }
//        return labels;
//    }

    @Named("insertLabelsIdToTask")
    public List<Label> insertLabelsIdToTask(List<Long> labelIds) {
        return labelRepository.findByIdIn(labelIds);
    }

//    @Named("labelsToStrings")
//    public List<String> labelsToStrings(List<Label> labels) {
//        List<String> result = new ArrayList<>();
//        for (Label label : labels) {
//            String name = label.getName();
//            result.add(name);
//        }
//        return result;
//    }

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
