package com.effix.api.repository;

import com.effix.api.model.entity.Recommendation;
import com.effix.api.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Recommendation findFirstByUserModelIdOrderByIdDesc(UserModel effixUserModel);

    List<Recommendation> findByUserModelId(UserModel effixUserModel);
}
