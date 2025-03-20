package com.effix.api.service;

import com.effix.api.dto.RecommendationRequestDTO;
import com.effix.api.model.entity.Recommendation;
import com.effix.api.model.entity.UserModel;

import java.util.List;

public interface RecommendationService {

    Recommendation index(UserModel effixUserModel);

    List<Recommendation> getAllRecommendation(UserModel effixUserModel);

    String create(RecommendationRequestDTO recommendationRequestDTO);
}
