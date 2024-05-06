package hexlet.code.app.controller;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class UserController {


    private UserMapper userMapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserUtils userUtils;

    @GetMapping
    public ResponseEntity<List<UserDTO>> index(@RequestParam(defaultValue = "0") int _start,
                                              @RequestParam(defaultValue = "10") int _end,
                                              @RequestParam(defaultValue = "id") String _sort,
                                              @RequestParam(defaultValue = "ASC") String _order) {
        int page = _start / (_end - _start);
        Sort.Direction direction = Sort.Direction.fromString(_order);
        Pageable pageable = PageRequest.of(page, _end - _start, Sort.by(direction, _sort));

        var result =  userRepository.findAll(pageable).stream()
                .map(userMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);

    }

    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", id)));
        return userMapper.map(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User user = userMapper.map(userCreateDTO);
        String login = user.getEmail();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(String.format("Username with login '%s' already exists", login));
        }
        return userMapper.map(user);
    }


    @PreAuthorize("@userUtils.isUserTheOwner(#id)")
    @PutMapping(path = "/{id}")
    public UserDTO update(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", id)));
        userMapper.update(userUpdateDTO, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("@userUtils.isUserTheOwner(#id)")
    public void delete(@PathVariable long id) {
        userRepository.deleteById(id);
    }
}
