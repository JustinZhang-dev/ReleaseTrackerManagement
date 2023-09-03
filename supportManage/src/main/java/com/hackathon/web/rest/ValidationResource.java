package com.hackathon.web.rest;

import com.hackathon.domain.Validation;
import com.hackathon.repository.ValidationRepository;
import com.hackathon.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hackathon.domain.Validation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ValidationResource {

    private final Logger log = LoggerFactory.getLogger(ValidationResource.class);

    private static final String ENTITY_NAME = "supportManageValidation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidationRepository validationRepository;

    public ValidationResource(ValidationRepository validationRepository) {
        this.validationRepository = validationRepository;
    }

    /**
     * {@code POST  /validations} : Create a new validation.
     *
     * @param validation the validation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validation, or with status {@code 400 (Bad Request)} if the validation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/validations")
    public ResponseEntity<Validation> createValidation(@RequestBody Validation validation) throws URISyntaxException {
        log.debug("REST request to save Validation : {}", validation);
        if (validation.getId() != null) {
            throw new BadRequestAlertException("A new validation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Validation result = validationRepository.save(validation);
        return ResponseEntity
            .created(new URI("/api/validations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /validations/:id} : Updates an existing validation.
     *
     * @param id the id of the validation to save.
     * @param validation the validation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validation,
     * or with status {@code 400 (Bad Request)} if the validation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/validations/{id}")
    public ResponseEntity<Validation> updateValidation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Validation validation
    ) throws URISyntaxException {
        log.debug("REST request to update Validation : {}, {}", id, validation);
        if (validation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Validation result = validationRepository.save(validation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /validations/:id} : Partial updates given fields of an existing validation, field will ignore if it is null
     *
     * @param id the id of the validation to save.
     * @param validation the validation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validation,
     * or with status {@code 400 (Bad Request)} if the validation is not valid,
     * or with status {@code 404 (Not Found)} if the validation is not found,
     * or with status {@code 500 (Internal Server Error)} if the validation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/validations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Validation> partialUpdateValidation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Validation validation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Validation partially : {}, {}", id, validation);
        if (validation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Validation> result = validationRepository
            .findById(validation.getId())
            .map(existingValidation -> {
                if (validation.getValidateId() != null) {
                    existingValidation.setValidateId(validation.getValidateId());
                }
                if (validation.getValidateRuleId() != null) {
                    existingValidation.setValidateRuleId(validation.getValidateRuleId());
                }
                if (validation.getValidateContent() != null) {
                    existingValidation.setValidateContent(validation.getValidateContent());
                }
                if (validation.getValidateResult() != null) {
                    existingValidation.setValidateResult(validation.getValidateResult());
                }
                if (validation.getValidateFlag() != null) {
                    existingValidation.setValidateFlag(validation.getValidateFlag());
                }

                return existingValidation;
            })
            .map(validationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validation.getId().toString())
        );
    }

    /**
     * {@code GET  /validations} : get all the validations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of validations in body.
     */
    @GetMapping("/validations")
    public List<Validation> getAllValidations() {
        log.debug("REST request to get all Validations");
        return validationRepository.findAll();
    }

    /**
     * {@code GET  /validations/:id} : get the "id" validation.
     *
     * @param id the id of the validation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/validations/{id}")
    public ResponseEntity<Validation> getValidation(@PathVariable Long id) {
        log.debug("REST request to get Validation : {}", id);
        Optional<Validation> validation = validationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(validation);
    }

    /**
     * {@code DELETE  /validations/:id} : delete the "id" validation.
     *
     * @param id the id of the validation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/validations/{id}")
    public ResponseEntity<Void> deleteValidation(@PathVariable Long id) {
        log.debug("REST request to delete Validation : {}", id);
        validationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
