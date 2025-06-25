package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusControllerTest {
    private final String url = "/api/task_statuses";
    private final String urlId = "/api/task_statuses/{id}";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    private TaskStatus testTaskStatus;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);
    }

    @Test
    void index() throws Exception {
        var response = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskStatusDTO> taskStatusDTOList = om.readValue(body, new TypeReference<>() { });

        var actual = taskStatusDTOList.stream().map(taskStatusMapper::map).toList();
        var expected = taskStatusRepository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void show() throws Exception {
        var request = get(urlId, testTaskStatus.getId());
        var result = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug()),
                v -> v.node("name").isEqualTo(testTaskStatus.getName())
        );
    }

    @Test
    void create() throws Exception {
        var data = Instancio.of(modelGenerator.getTaskStatusModel()).create();

        var request = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mvc.perform(request).andExpect(status().isCreated());

        var taskStatus = taskStatusRepository.findBySlug(data.getSlug()).orElseThrow();

        assertThat(taskStatus).isNotNull();
        assertThat(taskStatus.getSlug()).isEqualTo(data.getSlug());
        assertThat(taskStatus.getName()).isEqualTo(data.getName());
    }

    @Test
    void update() throws Exception {
        var data = new HashMap<>();
        data.put("name", "update");

        var request = put(urlId, testTaskStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mvc.perform(request).andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(testTaskStatus.getId()).orElseThrow();

        assertThat(taskStatus.getName()).isEqualTo("update");
    }
}
