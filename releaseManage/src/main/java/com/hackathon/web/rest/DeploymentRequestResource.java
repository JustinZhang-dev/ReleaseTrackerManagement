package com.hackathon.web.rest;

import com.hackathon.domain.DeploymentRequest;
import com.hackathon.repository.DeploymentRequestRepository;
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
 * REST controller for managing {@link com.hackathon.domain.DeploymentRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeploymentRequestResource {

    private final Logger log = LoggerFactory.getLogger(DeploymentRequestResource.class);

    private static final String ENTITY_NAME = "releaseManageDeploymentRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeploymentRequestRepository deploymentRequestRepository;

    public DeploymentRequestResource(DeploymentRequestRepository deploymentRequestRepository) {
        this.deploymentRequestRepository = deploymentRequestRepository;
    }

    /**
     * {@code POST  /deployment-requests} : Create a new deploymentRequest.
     *
     * @param deploymentRequest the deploymentRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deploymentRequest, or with status {@code 400 (Bad Request)} if the deploymentRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deployment-requests")
    public ResponseEntity<DeploymentRequest> createDeploymentRequest(@RequestBody DeploymentRequest deploymentRequest)
        throws URISyntaxException {
        log.debug("REST request to save DeploymentRequest : {}", deploymentRequest);
        if (deploymentRequest.getId() != null) {
            throw new BadRequestAlertException("A new deploymentRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeploymentRequest result = deploymentRequestRepository.save(deploymentRequest);
        return ResponseEntity
            .created(new URI("/api/deployment-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deployment-requests/:id} : Updates an existing deploymentRequest.
     *
     * @param id the id of the deploymentRequest to save.
     * @param deploymentRequest the deploymentRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deploymentRequest,
     * or with status {@code 400 (Bad Request)} if the deploymentRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deploymentRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deployment-requests/{id}")
    public ResponseEntity<DeploymentRequest> updateDeploymentRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeploymentRequest deploymentRequest
    ) throws URISyntaxException {
        log.debug("REST request to update DeploymentRequest : {}, {}", id, deploymentRequest);
        if (deploymentRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deploymentRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deploymentRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeploymentRequest result = deploymentRequestRepository.save(deploymentRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deploymentRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deployment-requests/:id} : Partial updates given fields of an existing deploymentRequest, field will ignore if it is null
     *
     * @param id the id of the deploymentRequest to save.
     * @param deploymentRequest the deploymentRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deploymentRequest,
     * or with status {@code 400 (Bad Request)} if the deploymentRequest is not valid,
     * or with status {@code 404 (Not Found)} if the deploymentRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the deploymentRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deployment-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeploymentRequest> partialUpdateDeploymentRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeploymentRequest deploymentRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeploymentRequest partially : {}, {}", id, deploymentRequest);
        if (deploymentRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deploymentRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deploymentRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeploymentRequest> result = deploymentRequestRepository
            .findById(deploymentRequest.getId())
            .map(existingDeploymentRequest -> {
                if (deploymentRequest.getDeployId() != null) {
                    existingDeploymentRequest.setDeployId(deploymentRequest.getDeployId());
                }
                if (deploymentRequest.getDeployGroupId() != null) {
                    existingDeploymentRequest.setDeployGroupId(deploymentRequest.getDeployGroupId());
                }
                if (deploymentRequest.getDeploySequence() != null) {
                    existingDeploymentRequest.setDeploySequence(deploymentRequest.getDeploySequence());
                }
                if (deploymentRequest.getAppId() != null) {
                    existingDeploymentRequest.setAppId(deploymentRequest.getAppId());
                }
                if (deploymentRequest.getDeployName() != null) {
                    existingDeploymentRequest.setDeployName(deploymentRequest.getDeployName());
                }
                if (deploymentRequest.getRequestComponent() != null) {
                    existingDeploymentRequest.setRequestComponent(deploymentRequest.getRequestComponent());
                }
                if (deploymentRequest.getDeployStatus() != null) {
                    existingDeploymentRequest.setDeployStatus(deploymentRequest.getDeployStatus());
                }
                if (deploymentRequest.getRequestType() != null) {
                    existingDeploymentRequest.setRequestType(deploymentRequest.getRequestType());
                }
                if (deploymentRequest.getReleaseType() != null) {
                    existingDeploymentRequest.setReleaseType(deploymentRequest.getReleaseType());
                }
                if (deploymentRequest.getModuleName() != null) {
                    existingDeploymentRequest.setModuleName(deploymentRequest.getModuleName());
                }
                if (deploymentRequest.getPortal() != null) {
                    existingDeploymentRequest.setPortal(deploymentRequest.getPortal());
                }
                if (deploymentRequest.getRequestorID() != null) {
                    existingDeploymentRequest.setRequestorID(deploymentRequest.getRequestorID());
                }
                if (deploymentRequest.getJiraId() != null) {
                    existingDeploymentRequest.setJiraId(deploymentRequest.getJiraId());
                }
                if (deploymentRequest.getPtsId() != null) {
                    existingDeploymentRequest.setPtsId(deploymentRequest.getPtsId());
                }
                if (deploymentRequest.getBuildId() != null) {
                    existingDeploymentRequest.setBuildId(deploymentRequest.getBuildId());
                }
                if (deploymentRequest.getRlmRequestId() != null) {
                    existingDeploymentRequest.setRlmRequestId(deploymentRequest.getRlmRequestId());
                }
                if (deploymentRequest.getRlmRequestEnv() != null) {
                    existingDeploymentRequest.setRlmRequestEnv(deploymentRequest.getRlmRequestEnv());
                }
                if (deploymentRequest.getDeploymentCategory() != null) {
                    existingDeploymentRequest.setDeploymentCategory(deploymentRequest.getDeploymentCategory());
                }
                if (deploymentRequest.getPromotionEnv() != null) {
                    existingDeploymentRequest.setPromotionEnv(deploymentRequest.getPromotionEnv());
                }
                if (deploymentRequest.getStartTime() != null) {
                    existingDeploymentRequest.setStartTime(deploymentRequest.getStartTime());
                }
                if (deploymentRequest.getEndTime() != null) {
                    existingDeploymentRequest.setEndTime(deploymentRequest.getEndTime());
                }
                if (deploymentRequest.getDeployCompletedDate() != null) {
                    existingDeploymentRequest.setDeployCompletedDate(deploymentRequest.getDeployCompletedDate());
                }
                if (deploymentRequest.getTriggerNextSeqDeploy() != null) {
                    existingDeploymentRequest.setTriggerNextSeqDeploy(deploymentRequest.getTriggerNextSeqDeploy());
                }
                if (deploymentRequest.getQaSignOffId() != null) {
                    existingDeploymentRequest.setQaSignOffId(deploymentRequest.getQaSignOffId());
                }
                if (deploymentRequest.getChgno() != null) {
                    existingDeploymentRequest.setChgno(deploymentRequest.getChgno());
                }

                return existingDeploymentRequest;
            })
            .map(deploymentRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deploymentRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /deployment-requests} : get all the deploymentRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deploymentRequests in body.
     */
    @GetMapping("/deployment-requests")
    public List<DeploymentRequest> getAllDeploymentRequests() {
        log.debug("REST request to get all DeploymentRequests");
        return deploymentRequestRepository.findAll();
    }

    /**
     * {@code GET  /deployment-requests/:id} : get the "id" deploymentRequest.
     *
     * @param id the id of the deploymentRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deploymentRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deployment-requests/{id}")
    public ResponseEntity<DeploymentRequest> getDeploymentRequest(@PathVariable Long id) {
        log.debug("REST request to get DeploymentRequest : {}", id);
        Optional<DeploymentRequest> deploymentRequest = deploymentRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deploymentRequest);
    }

    /**
     * {@code DELETE  /deployment-requests/:id} : delete the "id" deploymentRequest.
     *
     * @param id the id of the deploymentRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deployment-requests/{id}")
    public ResponseEntity<Void> deleteDeploymentRequest(@PathVariable Long id) {
        log.debug("REST request to delete DeploymentRequest : {}", id);
        deploymentRequestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
