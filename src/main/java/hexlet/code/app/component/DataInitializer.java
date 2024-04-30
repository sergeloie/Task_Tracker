package hexlet.code.app.component;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        String email = "hexlet@example.com";
        String password = "qwerty";
        User user = new User();
        user.setEmail(email);
        user.setPasswordDigest(passwordEncoder.encode(password));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
//            throw new IllegalArgumentException(String.format("Username with login '%s' already exists", email));
        }
    }
}
