package com.car.shop.core.service.mark;

import com.car.shop.core.exception.MarkNotFoundException;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.repository.MarkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MarkServiceImpl implements MarkService {

    @Autowired
    private MarkRepository markRepository;

    @Override
    public List<Mark> findAll() {
        log.info("Finding all marks");
        return markRepository.findAll();
    }

    @Override
    public Mark findById(Long id) {
        log.info("Finding mark by id: {}", id);
        return markRepository.findById(id)
                .orElseThrow(() -> new MarkNotFoundException(id));
    }

    @Override
    public Mark save(Mark mark) {
        log.info("Saving mark: {}", mark);
        return markRepository.save(mark);
    }

    @Override
    public Mark update(Long id, Mark newMark) {
        log.info("Updating mark by id: {} and body {} ", id, newMark);
        return markRepository.findById(id)
                .map(mark -> {
                            mark.setId(id);
                            mark.setName(newMark.getName());
                            mark.setProducingCountry(newMark.getProducingCountry());
                            return markRepository.save(mark);
                        }
                ).orElseThrow(
                        () -> new MarkNotFoundException(id)
                );
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting mark by id: {}", id);
        markRepository.deleteById(id);
    }
}
