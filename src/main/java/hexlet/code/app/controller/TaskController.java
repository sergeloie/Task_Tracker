package hexlet.code.app.controller;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class TaskController {
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    @GetMapping
    public List<TaskDTO> index() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map)
                .toList();
    }


    @GetMapping(path = "/{id}")
    public TaskDTO show(@PathVariable long id) {
        return taskMapper.map(taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task with id %d not found", id))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody @Valid TaskCreateDTO taskCreateDTO) {
        return taskMapper.map(taskRepository.save(taskMapper.map(taskCreateDTO)));
    }


    @PutMapping(path = "/{id}")
    public TaskDTO update(@PathVariable long id, @RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %d not found", id)));
        taskMapper.update(taskUpdateDTO, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }


    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        taskRepository.deleteById(id);
    }


}
