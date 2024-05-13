package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Task findByName(String name);


//    @Query("SELECT t FROM Task t WHERE (:titleCont is null or t.name like %:titleCont%) "
//            + "and (:assigneeId is null or t.assignee.id = :assigneeId) "
//            + "and (:status is null or t.taskStatus.slug = :status) "
//            + "and (:labelId is null or exists "
//            + "(select l from Label l join l.tasks t2 where l.id = :labelId and t2.id = t.id))")
//    List<Task> findAllByTitleContainingAndAssigneeIdAndStatusAndLabelId(
//            @Param("titleCont") String titleCont,
//            @Param("assigneeId") Long assigneeId,
//            @Param("status") String status,
//            @Param("labelId") Long labelId);



}
