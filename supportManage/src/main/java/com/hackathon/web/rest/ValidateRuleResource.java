package com.hackathon.web.rest;

import com.hackathon.domain.ValidateRule;
import com.hackathon.repository.ValidateRuleRepository;
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
 * REST controller for managing {@link com.hackathon.domain.ValidateRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ValidateRuleResource {

    private final Logger log = LoggerFactory.getLogger(ValidateRuleResource.class);

    private static final String ENTITY_NAME = "supportManageValidateRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidateRuleRepository validateRuleRepository;

    public ValidateRuleResource(ValidateRuleRepository validateRuleRepository) {
        this.validateRuleRepository = validateRuleRepository;
    }

    /**
     * {@code POST  /validate-rules} : Create a new validateRule.
     *
     * @param validateRule the validateRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validateRule, or with status {@code 400 (Bad Request)} if the validateRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/validate-rules")
    public ResponseEntity<ValidateRule> createValidateRule(@RequestBody ValidateRule validateRule) throws URISyntaxException {
        log.debug("REST request to save ValidateRule : {}", validateRule);
        if (validateRule.getId() != null) {
            throw new BadRequestAlertException("A new validateRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValidateRule result = validateRuleRepository.save(validateRule);
        return ResponseEntity
            .created(new URI("/api/validate-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /validate-rules/:id} : Updates an existing validateRule.
     *
     * @param id the id of the validateRule to save.
     * @param validateRule the validateRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validateRule,
     * or with status {@code 400 (Bad Request)} if the validateRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validateRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/validate-rules/{id}")
    public ResponseEntity<ValidateRule> updateValidateRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ValidateRule validateRule
    ) throws URISyntaxException {
        log.debug("REST request to update ValidateRule : {}, {}", id, validateRule);
        if (validateRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validateRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validateRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ValidateRule result = validateRuleRepository.save(validateRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validateRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /validate-rules/:id} : Partial updates given fields of an existing validateRule, field will ignore if it is null
     *
     * @param id the id of the validateRule to save.
     * @param validateRule the validateRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validateRule,
     * or with status {@code 400 (Bad Request)} if the validateRule is not valid,
     * or with status {@code 404 (Not Found)} if the validateRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the validateRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/validate-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ValidateRule> partialUpdateValidateRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ValidateRule validateRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update ValidateRule partially : {}, {}", id, validateRule);
        if (validateRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validateRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validateRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValidateRule> result = validateRuleRepository
            .findById(validateRule.getId())
            .map(existingValidateRule -> {
                if (validateRule.getValidateRuleId() != null) {
                    existingValidateRule.setValidateRuleId(validateRule.getValidateRuleId());
                }
                if (validateRule.getValidateRule() != null) {
                    existingValidateRule.setValidateRule(validateRule.getValidateRule());
                }

                return existingValidateRule;
            })
            .map(validateRuleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validateRule.getId().toString())
        );
    }

    /**
     * {@code GET  /validate-rules} : get all the validateRules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of validateRules in body.
     */
    @GetMapping("/validate-rules")
    public List<ValidateRule> getAllValidateRules() {
        log.debug("REST request to get all ValidateRules");
        return validateRuleRepository.findAll();
    }

    /**
     * {@code GET  /validate-rules/:id} : get the "id" validateRule.
     *
     * @param id the id of the validateRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validateRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/validate-rules/{id}")
    public ResponseEntity<ValidateRule> getValidateRule(@PathVariable Long id) {
        log.debug("REST request to get ValidateRule : {}", id);
        Optional<ValidateRule> validateRule = validateRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(validateRule);
    }

    /**
     * {@code DELETE  /validate-rules/:id} : delete the "id" validateRule.
     *
     * @param id the id of the validateRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/validate-rules/{id}")
    public ResponseEntity<Void> deleteValidateRule(@PathVariable Long id) {
        log.debug("REST request to delete ValidateRule : {}", id);
        validateRuleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
