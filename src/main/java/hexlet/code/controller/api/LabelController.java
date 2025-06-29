package hexlet.code.controller.api;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/labels")
public class LabelController {
    private final LabelService labelService;

    @GetMapping
    ResponseEntity<List<LabelDTO>> index() {
        var lebelDTOList = labelService.getAllLabel();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(lebelDTOList.size()))
                .body(lebelDTOList);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    LabelDTO show(@PathVariable Long id) {
        return labelService.findByIdLabel(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LabelDTO create(@RequestBody @Valid LabelCreateDTO labelDTO) {
        return labelService.createLabel(labelDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    LabelDTO update(@RequestBody @Valid LabelUpdateDTO labelDTO, @PathVariable Long id) {
        return labelService.updateLabel(labelDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}
