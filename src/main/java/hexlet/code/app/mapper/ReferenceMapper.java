package hexlet.code.app.mapper;

import hexlet.code.app.model.BaseEntity;
import hexlet.code.app.model.TaskStatus;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
@NoArgsConstructor
public class ReferenceMapper {

    private EntityManager entityManager;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("statusToTaskStatus")
    public TaskStatus statusToTaskStatus(String status) {
        return entityManager.createQuery("SELECT ts FROM TaskStatus ts WHERE ts.slug = :status", TaskStatus.class)
                .setParameter("status", status)
                .getSingleResult();
    }
}
