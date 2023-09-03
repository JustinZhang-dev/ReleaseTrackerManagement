package com.hackathon.repository;

import com.hackathon.domain.DeploymentRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeploymentRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeploymentRequestRepository extends JpaRepository<DeploymentRequest, Long> {}
