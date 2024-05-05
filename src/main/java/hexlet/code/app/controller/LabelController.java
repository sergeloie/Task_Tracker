package hexlet.code.app.controller;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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
@RequestMapping("/api/labels")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class LabelController {
    private LabelRepository labelRepository;
    private LabelMapper labelMapper;

    @GetMapping
    public List<LabelDTO> index() {
        return labelRepository.findAll().stream()
                .map(labelMapper::map)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public LabelDTO show(@PathVariable long id) {
        return labelMapper.map(labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Label with id %d not found", id))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@RequestBody @Valid LabelCreateDTO labelCreateDTO) {
        Label label = labelMapper.map(labelCreateDTO);
        try {
            labelRepository.save(label);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(String.format("Label with name '%s' already exists", label));
        }
        return labelMapper.map(label);
    }

    @PutMapping(path = "/{id}")
    public LabelDTO update(@PathVariable long id, @RequestBody @Valid LabelUpdateDTO labelUpdateDTO) {
        Label label = labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Label with id %d not found", id)));
        labelMapper.update(labelUpdateDTO, label);
        try {
            labelRepository.save(label);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(String.format("Label with name '%s' already exists", label));
        }
        return labelMapper.map(label);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        labelRepository.deleteById(id);
    }
}
