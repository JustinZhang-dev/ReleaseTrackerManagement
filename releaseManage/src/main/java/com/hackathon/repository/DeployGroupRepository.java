package com.hackathon.repository;

import com.hackathon.domain.DeployGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeployGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeployGroupRepository extends JpaRepository<DeployGroup, Long> {}
