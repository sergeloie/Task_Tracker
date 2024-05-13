package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        return withNameCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatusSlug(params.getStatus()))
                .and(withLabelId(params.getLabelId()));

    }

    private Specification<Task> withNameCont(String name) {
        return (root, query, cb) -> name == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name + "%");

    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withStatusSlug(String slug) {
        return (root, query, cb) -> slug == null
                ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), slug);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null
                ? cb.conjunction()
                : cb.equal(root.join("labels", JoinType.INNER).get("id"), labelId);
    }


}
