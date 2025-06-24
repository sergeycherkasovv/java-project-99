package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles(value = "development")
class LabelControllerTest {
    private final String url = "/api/labels";
    private final String urlId = "/api/labels/{id}";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    private Label testLabel;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
    }

    @Test
    void index() throws Exception {
        var response = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<LabelDTO> labelsDTO = om.readValue(body, new TypeReference<>() {});

        var actual = labelsDTO.stream().map(labelMapper::map).toList();
        var expected = labelRepository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void show() throws Exception {
        var request = get(urlId, testLabel.getId());
        var result = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    void create() throws Exception {
        var data = Instancio.of(modelGenerator.getLabelModel()).create();

        var request = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mvc.perform(request).andExpect(status().isCreated());

        var label = labelRepository.findByName(data.getName()).orElseThrow();

        assertThat(label).isNotNull();
        assertThat(label.getName()).isEqualTo(data.getName());
    }

    @Test
    void update() throws Exception {
        var data = new HashMap<>();
        data.put("name", "test");

        var request = put(urlId, testLabel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mvc.perform(request).andExpect(status().isOk());

        var label = labelRepository.findById(testLabel.getId()).orElseThrow();

        assertThat(label.getName()).isEqualTo("test");
    }
}