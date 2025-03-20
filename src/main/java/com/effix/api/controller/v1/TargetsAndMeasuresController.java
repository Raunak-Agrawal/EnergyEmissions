package com.effix.api.controller.v1;

import com.effix.api.dto.request.targetsandmeasures.CreateMeasuresDTO;
import com.effix.api.dto.request.targetsandmeasures.CreateTargetsDTO;
import com.effix.api.dto.request.targetsandmeasures.UpdateMeasuresDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.targetsandmeasures.MeasureResponseDTO;
import com.effix.api.dto.response.targetsandmeasures.TargetResponseDTO;
import com.effix.api.exception.EntityExistsException;
import com.effix.api.exception.NotFoundException;
import com.effix.api.exception.ServerException;
import com.effix.api.model.entity.Measure;
import com.effix.api.model.entity.Target;
import com.effix.api.service.TargetAndMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/target-and-measures")
public class TargetsAndMeasuresController {

    @Autowired
    private TargetAndMeasureService targetAndMeasureService;
    @PostMapping("/targets")
    public ResponseEntity<ResponseDTO<TargetResponseDTO>> createTarget(@RequestBody @Valid CreateTargetsDTO createTargetsDTO) throws ServerException, EntityExistsException {
        Target target = targetAndMeasureService.createTarget(createTargetsDTO);
        TargetResponseDTO responseDTO = TargetResponseDTO.builder()
                .id(target.getId())
                .reductionTarget(target.getReductionTarget())
                .emissionCategory(target.getEmissionCategory())
                .startDate(target.getStartDate())
                .createdAt(target.getCreatedAt().toString())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create target success", responseDTO));
    }

    @PostMapping("/measures")
    public ResponseEntity<ResponseDTO<List<MeasureResponseDTO>>> createMeasure(@RequestBody @Valid CreateMeasuresDTO createMeasuresDTO) throws ServerException, EntityExistsException {
        List<Measure> measures = targetAndMeasureService.createMeasure(createMeasuresDTO);
        List<MeasureResponseDTO> measureResponseDTOS = new ArrayList<>();
        measures.forEach(measure -> {
            MeasureResponseDTO responseDTO = MeasureResponseDTO.builder()
                    .id(measure.getId())
                    .emissionCategory(measure.getEmissionCategory())
                    .typeOfMeasure(measure.getTypeOfMeasure())
                    .startDate(measure.getStartDate())
                    .createdAt(measure.getCreatedAt().toString())
                    .build();
            measureResponseDTOS.add(responseDTO);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create measure success", measureResponseDTOS));
    }

    @PutMapping("/targets/{id}")
    public ResponseEntity<ResponseDTO<TargetResponseDTO>> updateTarget(@PathVariable Long id, @RequestBody @Valid CreateTargetsDTO createTargetsDTO) throws ServerException, NotFoundException {
        Target target = targetAndMeasureService.updateTarget(id, createTargetsDTO);
        TargetResponseDTO responseDTO = TargetResponseDTO.builder()
                .id(target.getId())
                .reductionTarget(target.getReductionTarget())
                .emissionCategory(target.getEmissionCategory())
                .startDate(target.getStartDate())
                .createdAt(target.getCreatedAt().toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Update target success", responseDTO));
    }

    @PutMapping("/measures/{id}")
    public ResponseEntity<ResponseDTO<MeasureResponseDTO>> updateMeasure(@PathVariable Long id, @RequestBody @Valid UpdateMeasuresDTO updateMeasuresDTO) throws ServerException, NotFoundException {
        Measure measure = targetAndMeasureService.updateMeasure(id, updateMeasuresDTO);
        MeasureResponseDTO responseDTO = MeasureResponseDTO.builder()
                .id(measure.getId())
                .emissionCategory(measure.getEmissionCategory())
                .typeOfMeasure(measure.getTypeOfMeasure())
                .startDate(measure.getStartDate())
                .createdAt(measure.getCreatedAt().toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Update measure success", responseDTO));
    }

    @GetMapping("/targets")
    public ResponseEntity<ResponseDTO<List<TargetResponseDTO>>> getAllTargets() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Targets fetched success", targetAndMeasureService.getAllTargets()));
    }

    @GetMapping("/measures")
    public ResponseEntity<ResponseDTO<List<MeasureResponseDTO>>> getAllMeasures() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Measures fetched success", targetAndMeasureService.getAllMeasures()));
    }
}
