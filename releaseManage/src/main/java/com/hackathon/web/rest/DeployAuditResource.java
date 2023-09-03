package com.hackathon.web.rest;

import com.hackathon.domain.DeployAudit;
import com.hackathon.repository.DeployAuditRepository;
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
 * REST controller for managing {@link com.hackathon.domain.DeployAudit}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeployAuditResource {

    private final Logger log = LoggerFactory.getLogger(DeployAuditResource.class);

    private static final String ENTITY_NAME = "releaseManageDeployAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeployAuditRepository deployAuditRepository;

    public DeployAuditResource(DeployAuditRepository deployAuditRepository) {
        this.deployAuditRepository = deployAuditRepository;
    }

    /**
     * {@code POST  /deploy-audits} : Create a new deployAudit.
     *
     * @param deployAudit the deployAudit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deployAudit, or with status {@code 400 (Bad Request)} if the deployAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deploy-audits")
    public ResponseEntity<DeployAudit> createDeployAudit(@RequestBody DeployAudit deployAudit) throws URISyntaxException {
        log.debug("REST request to save DeployAudit : {}", deployAudit);
        if (deployAudit.getId() != null) {
            throw new BadRequestAlertException("A new deployAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeployAudit result = deployAuditRepository.save(deployAudit);
        return ResponseEntity
            .created(new URI("/api/deploy-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deploy-audits/:id} : Updates an existing deployAudit.
     *
     * @param id the id of the deployAudit to save.
     * @param deployAudit the deployAudit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deployAudit,
     * or with status {@code 400 (Bad Request)} if the deployAudit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deployAudit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deploy-audits/{id}")
    public ResponseEntity<DeployAudit> updateDeployAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeployAudit deployAudit
    ) throws URISyntaxException {
        log.debug("REST request to update DeployAudit : {}, {}", id, deployAudit);
        if (deployAudit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deployAudit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deployAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeployAudit result = deployAuditRepository.save(deployAudit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deployAudit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deploy-audits/:id} : Partial updates given fields of an existing deployAudit, field will ignore if it is null
     *
     * @param id the id of the deployAudit to save.
     * @param deployAudit the deployAudit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deployAudit,
     * or with status {@code 400 (Bad Request)} if the deployAudit is not valid,
     * or with status {@code 404 (Not Found)} if the deployAudit is not found,
     * or with status {@code 500 (Internal Server Error)} if the deployAudit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deploy-audits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeployAudit> partialUpdateDeployAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeployAudit deployAudit
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeployAudit partially : {}, {}", id, deployAudit);
        if (deployAudit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deployAudit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deployAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeployAudit> result = deployAuditRepository
            .findById(deployAudit.getId())
            .map(existingDeployAudit -> {
                if (deployAudit.getAuditId() != null) {
                    existingDeployAudit.setAuditId(deployAudit.getAuditId());
                }
                if (deployAudit.getDeployId() != null) {
                    existingDeployAudit.setDeployId(deployAudit.getDeployId());
                }
                if (deployAudit.getDeployFormerStatus() != null) {
                    existingDeployAudit.setDeployFormerStatus(deployAudit.getDeployFormerStatus());
                }
                if (deployAudit.getDeployedStatus() != null) {
                    existingDeployAudit.setDeployedStatus(deployAudit.getDeployedStatus());
                }
                if (deployAudit.getDeployedOperationType() != null) {
                    existingDeployAudit.setDeployedOperationType(deployAudit.getDeployedOperationType());
                }
                if (deployAudit.getActionPerformedRole() != null) {
                    existingDeployAudit.setActionPerformedRole(deployAudit.getActionPerformedRole());
                }
                if (deployAudit.getActionPerformedBy() != null) {
                    existingDeployAudit.setActionPerformedBy(deployAudit.getActionPerformedBy());
                }

                return existingDeployAudit;
            })
            .map(deployAuditRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deployAudit.getId().toString())
        );
    }

    /**
     * {@code GET  /deploy-audits} : get all the deployAudits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deployAudits in body.
     */
    @GetMapping("/deploy-audits")
    public List<DeployAudit> getAllDeployAudits() {
        log.debug("REST request to get all DeployAudits");
        return deployAuditRepository.findAll();
    }

    /**
     * {@code GET  /deploy-audits/:id} : get the "id" deployAudit.
     *
     * @param id the id of the deployAudit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deployAudit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deploy-audits/{id}")
    public ResponseEntity<DeployAudit> getDeployAudit(@PathVariable Long id) {
        log.debug("REST request to get DeployAudit : {}", id);
        Optional<DeployAudit> deployAudit = deployAuditRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deployAudit);
    }

    /**
     * {@code DELETE  /deploy-audits/:id} : delete the "id" deployAudit.
     *
     * @param id the id of the deployAudit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deploy-audits/{id}")
    public ResponseEntity<Void> deleteDeployAudit(@PathVariable Long id) {
        log.debug("REST request to delete DeployAudit : {}", id);
        deployAuditRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
