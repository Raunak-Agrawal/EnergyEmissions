package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    @Transactional(readOnly = true)
    CompanyProfile findByUserModel(UserModel user);
}
