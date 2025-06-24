package com.data.project_javaWeb.service.admin.technology;

import com.data.project_javaWeb.dto.TechnologyDTO;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.entity.enums.Status;
import com.data.project_javaWeb.repository.admin.technology.TechnologyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TechnologyServiceImp implements TechnologyService {

    private final TechnologyRepository repository;
    public TechnologyServiceImp(TechnologyRepository repository) {
        this.repository = repository;
    }



    @Override
    public List<Technology> getAllTechnologies() {
        return repository.getAllTechnologies();
    }

    @Override
    public Page<Technology> list(String keyword, int page, int size) {
        long total = repository.count(keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return new PageImpl<>(repository.search(keyword, page, size), pageable, total);
    }

    @Override
    public TechnologyDTO get(Integer id) {
        Technology t = repository.findById(id);
        if (t == null) return null;
        TechnologyDTO dto = new TechnologyDTO();
        BeanUtils.copyProperties(t, dto);
        dto.setStatus(t.getStatus().name());
        return dto;
    }

    @Override
    public void saveOrUpdate(TechnologyDTO dto) {
        Technology exist = repository.findByName(dto.getName());
        if (dto.getId() == null) { // CREATE
            if (exist != null) {
                if (exist.getStatus() == Status.INACTIVE) {
                    exist.setStatus(Status.ACTIVE);
                    repository.update(exist);
                } else {
                    throw new RuntimeException("Tên đã tồn tại");
                }
            } else {
                Technology t = new Technology();
                BeanUtils.copyProperties(dto, t);
                repository.save(t);
            }
        } else { // UPDATE
            Technology current = repository.findById(dto.getId());
            if (current == null) throw new RuntimeException("Không tồn tại");
            if (!current.getName().equalsIgnoreCase(dto.getName())) {
                if (exist != null) throw new RuntimeException("Tên đã tồn tại");
            }
            current.setName(dto.getName());
            repository.update(current);
        }
    }

    @Override
    public void delete(Integer id) {
        Technology t = repository.findById(id);
        if (t == null) throw new RuntimeException("Không tồn tại");
        boolean hasFK = !t.getCandidates().isEmpty() || !t.getPositions().isEmpty();
        if (hasFK) {
            t.setStatus(Status.INACTIVE);
            repository.update(t);
        } else {
            repository.delete(t);
        }
    }

    @Override
    public List<Technology> findAllByNames(List<String> technologies) {
        if (technologies == null || technologies.isEmpty()) {
            return List.of(); // Return an empty list if no names are provided
        }
        return repository.findAllByNames(technologies);
    }
}
