package com.hackathon.web.rest;

import com.hackathon.domain.DeployGroup;
import com.hackathon.repository.DeployGroupRepository;
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
 * REST controller for managing {@link com.hackathon.domain.DeployGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeployGroupResource {

    private final Logger log = LoggerFactory.getLogger(DeployGroupResource.class);

    private static final String ENTITY_NAME = "releaseManageDeployGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeployGroupRepository deployGroupRepository;

    public DeployGroupResource(DeployGroupRepository deployGroupRepository) {
        this.deployGroupRepository = deployGroupRepository;
    }

    /**
     * {@code POST  /deploy-groups} : Create a new deployGroup.
     *
     * @param deployGroup the deployGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deployGroup, or with status {@code 400 (Bad Request)} if the deployGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deploy-groups")
    public ResponseEntity<DeployGroup> createDeployGroup(@RequestBody DeployGroup deployGroup) throws URISyntaxException {
        log.debug("REST request to save DeployGroup : {}", deployGroup);
        if (deployGroup.getId() != null) {
            throw new BadRequestAlertException("A new deployGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeployGroup result = deployGroupRepository.save(deployGroup);
        return ResponseEntity
            .created(new URI("/api/deploy-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deploy-groups/:id} : Updates an existing deployGroup.
     *
     * @param id the id of the deployGroup to save.
     * @param deployGroup the deployGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deployGroup,
     * or with status {@code 400 (Bad Request)} if the deployGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deployGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deploy-groups/{id}")
    public ResponseEntity<DeployGroup> updateDeployGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeployGroup deployGroup
    ) throws URISyntaxException {
        log.debug("REST request to update DeployGroup : {}, {}", id, deployGroup);
        if (deployGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deployGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deployGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeployGroup result = deployGroupRepository.save(deployGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deployGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deploy-groups/:id} : Partial updates given fields of an existing deployGroup, field will ignore if it is null
     *
     * @param id the id of the deployGroup to save.
     * @param deployGroup the deployGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deployGroup,
     * or with status {@code 400 (Bad Request)} if the deployGroup is not valid,
     * or with status {@code 404 (Not Found)} if the deployGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the deployGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deploy-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeployGroup> partialUpdateDeployGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeployGroup deployGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeployGroup partially : {}, {}", id, deployGroup);
        if (deployGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deployGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deployGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeployGroup> result = deployGroupRepository
            .findById(deployGroup.getId())
            .map(existingDeployGroup -> {
                if (deployGroup.getDeployGroupId() != null) {
                    existingDeployGroup.setDeployGroupId(deployGroup.getDeployGroupId());
                }
                if (deployGroup.getGroupName() != null) {
                    existingDeployGroup.setGroupName(deployGroup.getGroupName());
                }
                if (deployGroup.getAppId() != null) {
                    existingDeployGroup.setAppId(deployGroup.getAppId());
                }
                if (deployGroup.getDeployManagerId() != null) {
                    existingDeployGroup.setDeployManagerId(deployGroup.getDeployManagerId());
                }

                return existingDeployGroup;
            })
            .map(deployGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deployGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /deploy-groups} : get all the deployGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deployGroups in body.
     */
    @GetMapping("/deploy-groups")
    public List<DeployGroup> getAllDeployGroups() {
        log.debug("REST request to get all DeployGroups");
        return deployGroupRepository.findAll();
    }

    /**
     * {@code GET  /deploy-groups/:id} : get the "id" deployGroup.
     *
     * @param id the id of the deployGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deployGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deploy-groups/{id}")
    public ResponseEntity<DeployGroup> getDeployGroup(@PathVariable Long id) {
        log.debug("REST request to get DeployGroup : {}", id);
        Optional<DeployGroup> deployGroup = deployGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deployGroup);
    }

    /**
     * {@code DELETE  /deploy-groups/:id} : delete the "id" deployGroup.
     *
     * @param id the id of the deployGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deploy-groups/{id}")
    public ResponseEntity<Void> deleteDeployGroup(@PathVariable Long id) {
        log.debug("REST request to delete DeployGroup : {}", id);
        deployGroupRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
