package hexlet.code.app.controller;

import hexlet.code.app.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.app.dto.taskstatus.TaskStatusDTO;
import hexlet.code.app.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class TaskStatusController {

    private TaskStatusMapper taskStatusMapper;
    private TaskStatusRepository taskStatusRepository;

    @GetMapping
    public List<TaskStatusDTO> index() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable long id) {
        TaskStatus result = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task status with id %d not found", id)));
        return taskStatusMapper.map(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO taskStatusCreateDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(taskStatusCreateDTO);
        try {
            taskStatusRepository.save(taskStatus);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                    String.format("Task status with name = '%s' or slug = '%s' already exists",
                    taskStatus.getName(),
                    taskStatus.getSlug()));
        }
        return taskStatusMapper.map(taskStatus);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusDTO update(@PathVariable long id, @Valid @RequestBody TaskStatusUpdateDTO taskStatusUpdateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task status with id %d not found", id)));
        taskStatusMapper.update(taskStatusUpdateDTO, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        taskStatusRepository.deleteById(id);
    }
}
