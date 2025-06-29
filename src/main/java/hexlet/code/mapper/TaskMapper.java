package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "labels", target = "taskLabelIds", qualifiedByName = "toIdByLabels")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toTaskStatusBySlug")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabelsById")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toTaskStatusBySlug")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabelsById")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toTaskStatusBySlug")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabelsById")
    public abstract Task map(TaskDTO dto);

    @Named("toTaskStatusBySlug")
    public TaskStatus toTaskStatusBySlug(String slug) {
        return slug != null
                ? taskStatusRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Not slug: " + slug))
                : null;
    }

    @Named("toLabelsById")
    public Set<Label> toLabelsById(Set<Long> labelsId) {
        return labelsId == null || labelsId.isEmpty()
                ? new HashSet<>()
                : labelRepository.findAllById(labelsId)
                                    .stream()
                                    .collect(Collectors.toSet());
    }

    @Named("toIdByLabels")
    public Set<Long> toIdByLabels(Set<Label> labels) {
        return labels == null || labels.isEmpty()
                ? new HashSet<>()
                : labels.stream()
                    .map(Label::getId)
                    .collect(Collectors.toSet());
    }
}
