package com.effix.api.service;

import com.effix.api.dto.request.targetsandmeasures.CreateMeasuresDTO;
import com.effix.api.dto.request.targetsandmeasures.CreateTargetsDTO;
import com.effix.api.dto.request.targetsandmeasures.UpdateMeasuresDTO;
import com.effix.api.dto.response.targetsandmeasures.MeasureResponseDTO;
import com.effix.api.dto.response.targetsandmeasures.TargetResponseDTO;
import com.effix.api.exception.EntityExistsException;
import com.effix.api.exception.NotFoundException;
import com.effix.api.model.entity.Measure;
import com.effix.api.model.entity.Target;

import java.util.List;

public interface TargetAndMeasureService {
    Target createTarget(CreateTargetsDTO createTargetsDTO) throws EntityExistsException;

    List<Measure> createMeasure(CreateMeasuresDTO createMeasuresDTO) throws EntityExistsException;

    Target updateTarget(Long id, CreateTargetsDTO createTargetsDTO) throws NotFoundException;

    Measure updateMeasure(Long id, UpdateMeasuresDTO updateMeasuresDTO) throws NotFoundException;

    List<TargetResponseDTO> getAllTargets();

    List<MeasureResponseDTO> getAllMeasures();
}
