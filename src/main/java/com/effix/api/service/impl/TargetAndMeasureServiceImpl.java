package com.effix.api.service.impl;

import com.effix.api.dto.request.targetsandmeasures.CreateMeasuresDTO;
import com.effix.api.dto.request.targetsandmeasures.CreateTargetsDTO;
import com.effix.api.dto.request.targetsandmeasures.UpdateMeasuresDTO;
import com.effix.api.dto.response.targetsandmeasures.MeasureResponseDTO;
import com.effix.api.dto.response.targetsandmeasures.TargetResponseDTO;
import com.effix.api.exception.EntityExistsException;
import com.effix.api.exception.NotFoundException;
import com.effix.api.model.entity.Measure;
import com.effix.api.model.entity.Target;
import com.effix.api.repository.MeasureRepository;
import com.effix.api.repository.TargetRepository;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.TargetAndMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TargetAndMeasureServiceImpl implements TargetAndMeasureService {

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private MeasureRepository measureRepository;
    @Autowired
    private CompanyProfileService companyProfileService;
    @Override
    public Target createTarget(CreateTargetsDTO createTargetsDTO) throws EntityExistsException {
        Target targetWithExistingEmissionCategory = targetRepository.findByEmissionCategory(createTargetsDTO.getEmissionCategory());
        if(targetWithExistingEmissionCategory != null){
            throw new EntityExistsException("Target with emission category already exists");
        }
        Target target = Target.builder()
                .reductionTarget(createTargetsDTO.getReductionTarget())
                .companyProfile(companyProfileService.getCompanyProfile())
                .emissionCategory(createTargetsDTO.getEmissionCategory())
                .startDate(createTargetsDTO.getStartDate())
                .build();
        return targetRepository.save(target);
    }

    @Override
    public List<Measure> createMeasure(CreateMeasuresDTO createMeasuresDTO) throws EntityExistsException {
        List<Measure> measureWithExistingEmissionCategory = measureRepository.findByEmissionCategory(createMeasuresDTO.getEmissionCategory());
        if(measureWithExistingEmissionCategory != null && !measureWithExistingEmissionCategory.isEmpty()){
            throw new EntityExistsException("Measure with emission category already exists");
        }

        List<Measure> measures = new ArrayList<>();

        createMeasuresDTO.getTypeOfMeasure().forEach(m -> {
            Measure measure = Measure.builder()
                    .typeOfMeasure(m)
                    .companyProfile(companyProfileService.getCompanyProfile())
                    .emissionCategory(createMeasuresDTO.getEmissionCategory())
                    .startDate(createMeasuresDTO.getStartDate())
                    .build();
            measures.add(measureRepository.save(measure));
        });
        return measures;
    }

    @Override
    public Target updateTarget(Long id, CreateTargetsDTO createTargetsDTO) throws NotFoundException {
        Optional<Target> targetOptional = targetRepository.findById(id);
        if(targetOptional.isEmpty()) {
            throw new NotFoundException("target not found");
        }
        Target target = targetOptional.get();

        target.setReductionTarget(createTargetsDTO.getReductionTarget());
        target.setEmissionCategory(createTargetsDTO.getEmissionCategory());
        target.setStartDate(createTargetsDTO.getStartDate());

        return targetRepository.save(target);
    }

    @Override
    public Measure updateMeasure(Long id, UpdateMeasuresDTO updateMeasuresDTO) throws NotFoundException {
        Optional<Measure> measureOptional = measureRepository.findById(id);
        if(measureOptional.isEmpty()) {
            throw new NotFoundException("measure not found");
        }

        Measure measure = measureOptional.get();

        measure.setTypeOfMeasure(updateMeasuresDTO.getTypeOfMeasure());
        measure.setEmissionCategory(updateMeasuresDTO.getEmissionCategory());
        measure.setStartDate(updateMeasuresDTO.getStartDate());

        return measureRepository.save(measure);
    }

    @Override
    public List<TargetResponseDTO> getAllTargets() {
        return targetRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(target -> TargetResponseDTO.builder()
                        .id(target.getId())
                        .emissionCategory(target.getEmissionCategory())
                        .startDate(target.getStartDate())
                        .reductionTarget(target.getReductionTarget())
                        .createdAt(target.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MeasureResponseDTO> getAllMeasures() {
        return measureRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(measure -> MeasureResponseDTO.builder()
                        .id(measure.getId())
                        .emissionCategory(measure.getEmissionCategory())
                        .startDate(measure.getStartDate())
                        .typeOfMeasure(measure.getTypeOfMeasure())
                        .createdAt(measure.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }
}
