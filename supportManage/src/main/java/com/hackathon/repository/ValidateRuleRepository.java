package com.hackathon.repository;

import com.hackathon.domain.ValidateRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ValidateRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidateRuleRepository extends JpaRepository<ValidateRule, Long> {}
