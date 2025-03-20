package com.effix.api.repository;

import com.effix.api.enums.EmissionFactorType;
import com.effix.api.model.entity.EmissionFactor;
import com.effix.api.model.entity.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactor, Long> {
//    @Query("SELECT e FROM emission_factor e WHERE e.category = ?1 AND e.sub_category_id = ?2 AND e.unit = ?3")
    EmissionFactor findByCategoryAndSubCategoryAndUnit(EmissionFactorType emissionFactorType, Lookup subCategory, String unit);

    EmissionFactor findByCategoryAndSubCategory(EmissionFactorType emissionFactorType, Lookup subCategory);

//    EmissionFactor findByCategoryAndSubCategoryCountry(String category, String country);

//    List<EmissionFactor> findByCategory(EmissionFactorType category);
}
