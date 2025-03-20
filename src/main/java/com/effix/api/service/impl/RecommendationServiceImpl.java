package com.effix.api.service.impl;

import com.effix.api.dto.RecommendationRequestDTO;
import com.effix.api.model.entity.Recommendation;
import com.effix.api.model.entity.UserModel;
import com.effix.api.repository.RecommendationRepository;
import com.effix.api.repository.UserRepository;
import com.effix.api.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Recommendation index(UserModel effixUserModel) {
        return recommendationRepository.findFirstByUserModelIdOrderByIdDesc(effixUserModel);
    }

    @Override
    public List<Recommendation> getAllRecommendation(UserModel effixUserModel){
        List<Recommendation> recommendationList = recommendationRepository.findByUserModelId(effixUserModel);
        return recommendationList;

    }

    @Override
    public String create(RecommendationRequestDTO recommendationRequestDTO) {
        Recommendation recommendation = Recommendation.builder()
                .areaForSolarPanel(recommendationRequestDTO.getAreaForSolarPanel())
                .discountRate(recommendationRequestDTO.getDiscountRate())
                .capitolRecoveryFactor(recommendationRequestDTO.getCapitolRecoveryFactor())
                .investmentTimeHorizon(recommendationRequestDTO.getInvestmentTimeHorizon())
                .userModelId(userRepository.getById(recommendationRequestDTO.getUser()))
                .build();
        recommendationRepository.save(recommendation);
        return "Success";
    }
}
