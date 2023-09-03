package com.hackathon.repository;

import com.hackathon.domain.QASignOff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QASignOff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QASignOffRepository extends JpaRepository<QASignOff, Long> {}
