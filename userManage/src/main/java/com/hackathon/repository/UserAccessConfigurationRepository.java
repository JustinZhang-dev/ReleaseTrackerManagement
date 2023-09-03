package com.hackathon.repository;

import com.hackathon.domain.UserAccessConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAccessConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAccessConfigurationRepository extends JpaRepository<UserAccessConfiguration, Long> {}
