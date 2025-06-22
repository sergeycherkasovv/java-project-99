package hexlet.code.component;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final TaskStatusService taskStatusService;

    @Autowired
    private final TaskService taskService;

    @Autowired
    private final LabelService labelService;

    @Autowired
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";

        // User
        var userData = new UserCreateDTO();
        userData.setFirstName("Sergey");
        userData.setLastName("Cherkasov");
        userData.setEmail(email);
        userData.setPassword("qwerty");
        userService.createUser(userData);

        // TaskStatus
        var taskStatusData = Map.of(
                "Draft", "draft",
                "ToReview", "to_review",
                "ToBeFixed", "to_be_fixed",
                "ToPublish", "to_publish",
                "Published", "published"
        );

        taskStatusData.forEach((k, v) -> {
            var taskStatus = new TaskStatusCreateDTO();
            taskStatus.setName(k);
            taskStatus.setSlug(v);
            taskStatusService.createTaskStatus(taskStatus);
        });

        // Task
        var user = userRepository.findByEmail(email).get();

        taskStatusData.values().forEach(v ->
            IntStream.range(1, 10).forEach(i -> {
                var task = new TaskCreateDTO();
                task.setIndex(faker.number().numberBetween(1, 1000));
                task.setAssigneeId(user.getId());
                task.setTitle(faker.name().title());
                task.setContent(faker.hobbit().quote());
                task.setStatus(v);
                taskService.createTask(task);
            })
        );

        // Label
        var labelData = List.of(
                "feature",
                "bug"
        );

        labelData.forEach(l -> {
            var label = new LabelCreateDTO();
            label.setName(l);
            labelService.createLabel(label);
        });
    }
}
