package hexlet.code.app;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SomeTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void dummyTest() {
        List<User> list = userRepository.findAll();
        System.out.println(list);
    }
}
