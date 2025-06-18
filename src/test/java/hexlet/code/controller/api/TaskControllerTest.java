package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
    private final String url = "/api/tasks";
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
    private TaskMapper taskMapper;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        taskRepository.save(testTask);
    }

    @Test
    void testIndex() throws Exception {
        var response = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskDTO> taskDTOList = om.readValue(body, new TypeReference<>() {});

        var actual = taskDTOList.stream().map(taskMapper::map).toList();
        var expected = taskRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {
        var request = get(url + "/{id}", testTask.getId());
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
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId())
        );
    }

    @Test
    void testCreate() throws Exception {
        var createData = new TaskCreateDTO();
        createData.setIndex(testTask.getIndex());
        createData.setTitle("TestTask");
        createData.setContent(testTask.getDescription());
        createData.setStatus(testTask.getTaskStatus().getSlug());
        createData.setAssigneeId(testTask.getAssignee().getId());

        var request = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createData));

        mvc.perform(request).andExpect(status().isCreated());

        var task = taskRepository.findByName(createData.getTitle()).orElse(null);

        assertThat(task).isNotNull();
        assertThat(task.getIndex()).isEqualTo(createData.getIndex());
        assertThat(task.getDescription()).isEqualTo(createData.getContent());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(createData.getStatus());
        assertThat(task.getAssignee().getId()).isEqualTo(createData.getAssigneeId());
    }

    @Test
    void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("index", 12345);

        var request = put(url + "/{id}", testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mvc.perform(request).andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(task.getIndex()).isEqualTo(12345);
    }
}