package com.hackathon.repository;

import com.hackathon.domain.Validation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Validation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {}
