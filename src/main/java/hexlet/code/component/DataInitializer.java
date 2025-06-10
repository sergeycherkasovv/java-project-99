package hexlet.code.component;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateDTO();
        userData.setFirstName("Sergey");
        userData.setLastName("Cherkasov");
        userData.setEmail("manager@example.com");
        userData.setPassword("qwerty");
        userService.createUser(userData);
    }
}
