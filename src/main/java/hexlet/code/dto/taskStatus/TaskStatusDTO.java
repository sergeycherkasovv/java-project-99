package hexlet.code.dto.taskStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusDTO {
    private Long id;
    private String slug;
    private String name;
    private LocalDate createdAt;
}
