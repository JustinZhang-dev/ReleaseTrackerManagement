package com.hackathon.repository;

import com.hackathon.domain.DeployAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeployAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeployAuditRepository extends JpaRepository<DeployAudit, Long> {}
