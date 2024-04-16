package hexlet.code.app.controller;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {


    private UserMapper userMapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDTO> index() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
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
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        userRepository.save(user);
        return userMapper.map(user);
    }

//    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping(path = "/{id}")
    public UserDTO update(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found", id)));
        userMapper.update(userUpdateDTO, user);
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        userRepository.save(user);
        return userMapper.map(user);
    }
//    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        userRepository.deleteById(id);
    }
}
