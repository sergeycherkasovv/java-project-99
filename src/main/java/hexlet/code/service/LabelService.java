package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public List<LabelDTO> getAllLabel() {
        var label = labelRepository.findAll();

        return label.stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDTO findByIdLabel(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        return labelMapper.map(label);
    }

    public LabelDTO createLabel(LabelCreateDTO labelDTO) {
        var label = labelMapper.map(labelDTO);
        labelRepository.save(label);

        return labelMapper.map(label);
    }

    public LabelDTO updateLabel(LabelUpdateDTO labelDTO, Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        labelMapper.update(labelDTO, label);
        labelRepository.save(label);

        return labelMapper.map(label);
    }

    public void deleteLabel(Long id) {
        labelRepository.deleteById(id);
    }
}
