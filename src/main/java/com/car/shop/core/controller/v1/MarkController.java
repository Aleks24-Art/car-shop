package com.car.shop.core.controller.v1;

import com.car.shop.core.model.dto.mark.CreateMarkDto;
import com.car.shop.core.model.dto.mark.MarkDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.service.mark.MarkService;
import com.car.shop.core.util.mapper.MarkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class MarkController {

    @Autowired
    private MarkService markService;

    @Autowired
    private MarkMapper markMapper;

    @GetMapping("v1/mark/all")
    public List<MarkDto> findAllMarks() {
        return markMapper.toDtos(markService.findAll());
    }

    @GetMapping("v1/mark/{id}")
    public MarkDto findMarkById(@PathVariable @Positive(message = "ID не может быть меньше нуля") Long id) {
        return markMapper.toDto(markService.findById(id));
    }

    @PostMapping("v1/mark")
    public MarkDto saveMark(@RequestBody @Valid CreateMarkDto createMarkDto) {
        Mark markToSave = markMapper.toMark(createMarkDto);
        Mark savedMark = markService.save(markToSave);
        return markMapper.toDto(savedMark);
    }

    @DeleteMapping("v1/mark/{id}")
    public ResponseEntity<String> deleteMark(@PathVariable @Positive(message = "ID не может быть меньше нуля") Long id) {
        markService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
