package com.effix.api.controller.v1;

import com.effix.api.dto.RecommendationRequestDTO;
import com.effix.api.model.entity.UserModel;
import com.effix.api.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/api/v1/recommendations")
public class RecommendationController {

    @Autowired
    RecommendationService recommendationService;

    @GetMapping(value = "/{user}")
    public ResponseEntity index(@PathVariable("user") UserModel effixUserModel){
        return ResponseEntity.ok().body(recommendationService.getAllRecommendation(effixUserModel));
    }

    @PostMapping(value = "/")
    public ResponseEntity create(@RequestBody @Valid RecommendationRequestDTO recommendationRequestDTO){
        return ResponseEntity.ok().body(recommendationService.create(recommendationRequestDTO));
    }
}
