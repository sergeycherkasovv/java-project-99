package hexlet.code.mapper;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.BaseEntity;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("toTaskStatusBySlug")
    public TaskStatus toTaskStatusBySlug(String slug) {
        return slug != null
                ? taskStatusRepository
                    .findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Not slug: " + slug))
                : null;
    }

    @Named("toLabelsById")
    public List<Label> toLabelsById(List<Long> labelsId) {
        return labelsId == null || labelsId.isEmpty()
                ? new ArrayList<>()
                : labelsId.stream().map(this::getLabelById).toList();
    }

    @Named("toIdByLabels")
    public List<Long> toIdByLabels(List<Label> labels) {
        return labels == null || labels.isEmpty()
                ? new ArrayList<>()
                : labels.stream().map(Label::getId)
                .toList();
    }

    private Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
    }


}
