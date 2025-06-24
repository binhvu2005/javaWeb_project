package com.data.project_javaWeb.service.admin.recruitmentPosition;

import com.data.project_javaWeb.dto.RecruitmentPositionDTO;
import com.data.project_javaWeb.entity.RecruitmentPosition;
import com.data.project_javaWeb.entity.Technology;
import com.data.project_javaWeb.repository.admin.recruitmentPosition.RecruitmentPositionRepository;
import com.data.project_javaWeb.repository.admin.technology.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecruitmentPositionServiceImp implements RecruitmentPositionService {

    @Autowired
    private RecruitmentPositionRepository recruitmentPositionRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Override
    public Page<RecruitmentPositionDTO> getAll(int page, int size, String keyword) {
        Page<RecruitmentPosition> entities = recruitmentPositionRepository.findAll(page, size, keyword);
        List<RecruitmentPositionDTO> dtoList = entities.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
    }

    @Override
    public RecruitmentPositionDTO findById(Integer id) {
        RecruitmentPosition entity = recruitmentPositionRepository.findById(id);
        return toDTO(entity);
    }

    @Override
    public void save(RecruitmentPositionDTO dto) {
        RecruitmentPosition entity = toEntity(dto);
        recruitmentPositionRepository.save(entity);
    }

    @Override
    public void update(RecruitmentPositionDTO dto) {
        RecruitmentPosition entity = toEntity(dto);
        recruitmentPositionRepository.update(entity);
    }

    @Override
    public void delete(Integer id) {
        recruitmentPositionRepository.delete(id);
    }

    @Override
    public Page<RecruitmentPositionDTO> searchByKeywordAndTech(String keyword, String tech, int page, int size) {
        Page<RecruitmentPosition> entities = recruitmentPositionRepository.searchByKeywordAndTech(keyword, tech, page, size);
        List<RecruitmentPositionDTO> dtoList = entities.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
    }

    private RecruitmentPositionDTO toDTO(RecruitmentPosition entity) {
        RecruitmentPositionDTO dto = new RecruitmentPositionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setMinSalary(entity.getMinSalary());
        dto.setMaxSalary(entity.getMaxSalary());
        dto.setMinExperience(entity.getMinExperience());
        dto.setExpiredDate(entity.getExpiredDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setDescription(entity.getDescription());

        // Sử dụng tên công nghệ
        dto.setTechnologies(entity.getTechnologies().stream()
                .map(Technology::getName)
                .collect(Collectors.toList()));

        return dto;
    }

    private RecruitmentPosition toEntity(RecruitmentPositionDTO dto) {
        RecruitmentPosition entity;
        if (dto.getId() != null) {
            entity = recruitmentPositionRepository.findById(dto.getId());
        } else {
            entity = new RecruitmentPosition();
            entity.setCreatedDate(LocalDate.now());
        }

        entity.setName(dto.getName());
        entity.setMinSalary(dto.getMinSalary());
        entity.setMaxSalary(dto.getMaxSalary());
        entity.setMinExperience(dto.getMinExperience());
        entity.setExpiredDate(dto.getExpiredDate());
        entity.setDescription(dto.getDescription());

        // Tìm theo tên công nghệ
        List<Technology> techs = dto.getTechnologies().stream()
                .map(technologyRepository::findByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        entity.setTechnologies(new HashSet<>(techs));
        return entity;
    }
    // RecruitmentPositionServiceImpl.java
    @Override
    public RecruitmentPosition findEntityById(Integer id) {
        return recruitmentPositionRepository.findEntityById(id);
    }

}
