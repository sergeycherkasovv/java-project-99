package hexlet.code.controller.api;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping
    ResponseEntity<List<LabelDTO>> index() {
        var lebelDTOList = labelService.getAllLabel();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(lebelDTOList.size()))
                .body(lebelDTOList);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        return labelService.findByIdLabel(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@RequestBody @Valid LabelCreateDTO labelDTO) {
        return labelService.createLabel(labelDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@RequestBody @Valid LabelUpdateDTO labelDTO, @PathVariable Long id) {
        return labelService.updateLabel(labelDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}
