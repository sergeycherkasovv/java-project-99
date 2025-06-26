package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
    private final String url = "/api/tasks";
    private final String urlId = "/api/tasks/{id}";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskMapper taskMapper;

    private Task testTask;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();

        var status = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        var user = Instancio.of(modelGenerator.getUserModel()).create();
        var label = Instancio.of(modelGenerator.getLabelModel()).create();

        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(taskStatusRepository.save(status));
        testTask.setAssignee(userRepository.save(user));
        testTask.setLabels(Set.of(labelRepository.save(label)));

        taskRepository.save(testTask);
    }

    @Test
    void index() throws Exception {
        var response = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskDTO> taskDTOList = om.readValue(body, new TypeReference<>() { });

        var actual = taskDTOList.stream().map(taskMapper::map).toList();
        var expected = taskRepository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void filteredIndex() throws Exception {
        var assigneeId = testTask.getAssignee().getId();

        var response = mvc
                .perform(get("/api/tasks?assigneeId=" + assigneeId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThatJson(body)
                .isArray()
                .first()
                .node("assignee_id").isEqualTo(assigneeId);
    }

    @Test
    void show() throws Exception {
        var request = get(urlId, testTask.getId());
        var result = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("index").isEqualTo(testTask.getIndex()),
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("taskLabelIds").isArray().hasSizeGreaterThan(0)
        );
    }

    @Test
    void create() throws Exception {
        var createData = new TaskCreateDTO();
        createData.setIndex(testTask.getIndex());
        createData.setTitle(testTask.getName().toLowerCase());
        createData.setContent(testTask.getDescription());
        createData.setStatus(testTask.getTaskStatus().getSlug());
        createData.setAssigneeId(testTask.getAssignee().getId());

        var request = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createData));

        mvc.perform(request).andExpect(status().isCreated());

        var task = taskRepository.findByName(createData.getTitle()).orElseThrow();

        assertThat(task).isNotNull();
        assertThat(task.getIndex()).isEqualTo(createData.getIndex());
        assertThat(task.getDescription()).isEqualTo(createData.getContent());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(createData.getStatus());
        assertThat(task.getAssignee().getId()).isEqualTo(createData.getAssigneeId());
        assertThat(task.getName()).isEqualTo(createData.getTitle());
    }

    @Test
    void update() throws Exception {
        var taskId = testTask.getId();

        var data = new HashMap<>();
        data.put("index", 12345);

        var request = put(urlId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mvc.perform(request).andExpect(status().isOk());

        var task = taskRepository.findById(taskId).orElseThrow();

        assertThat(task.getIndex()).isEqualTo(12345);
    }

    @Test
    void destroy() throws Exception {
        var taskId = testTask.getId();

        mvc.perform(delete(urlId, taskId))
                .andExpect(status().isNoContent());

        var task = taskRepository.existsById(taskId);
        assertThat(task).isFalse();
    }
}

