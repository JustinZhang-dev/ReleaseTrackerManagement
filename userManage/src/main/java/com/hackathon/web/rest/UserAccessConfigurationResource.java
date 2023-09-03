package com.hackathon.web.rest;

import com.hackathon.domain.UserAccessConfiguration;
import com.hackathon.repository.UserAccessConfigurationRepository;
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
 * REST controller for managing {@link com.hackathon.domain.UserAccessConfiguration}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserAccessConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(UserAccessConfigurationResource.class);

    private static final String ENTITY_NAME = "userManageUserAccessConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAccessConfigurationRepository userAccessConfigurationRepository;

    public UserAccessConfigurationResource(UserAccessConfigurationRepository userAccessConfigurationRepository) {
        this.userAccessConfigurationRepository = userAccessConfigurationRepository;
    }

    /**
     * {@code POST  /user-access-configurations} : Create a new userAccessConfiguration.
     *
     * @param userAccessConfiguration the userAccessConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccessConfiguration, or with status {@code 400 (Bad Request)} if the userAccessConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-access-configurations")
    public ResponseEntity<UserAccessConfiguration> createUserAccessConfiguration(
        @RequestBody UserAccessConfiguration userAccessConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to save UserAccessConfiguration : {}", userAccessConfiguration);
        if (userAccessConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new userAccessConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAccessConfiguration result = userAccessConfigurationRepository.save(userAccessConfiguration);
        return ResponseEntity
            .created(new URI("/api/user-access-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-access-configurations/:id} : Updates an existing userAccessConfiguration.
     *
     * @param id the id of the userAccessConfiguration to save.
     * @param userAccessConfiguration the userAccessConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccessConfiguration,
     * or with status {@code 400 (Bad Request)} if the userAccessConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccessConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-access-configurations/{id}")
    public ResponseEntity<UserAccessConfiguration> updateUserAccessConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAccessConfiguration userAccessConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to update UserAccessConfiguration : {}, {}", id, userAccessConfiguration);
        if (userAccessConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccessConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccessConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAccessConfiguration result = userAccessConfigurationRepository.save(userAccessConfiguration);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccessConfiguration.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-access-configurations/:id} : Partial updates given fields of an existing userAccessConfiguration, field will ignore if it is null
     *
     * @param id the id of the userAccessConfiguration to save.
     * @param userAccessConfiguration the userAccessConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccessConfiguration,
     * or with status {@code 400 (Bad Request)} if the userAccessConfiguration is not valid,
     * or with status {@code 404 (Not Found)} if the userAccessConfiguration is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAccessConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-access-configurations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAccessConfiguration> partialUpdateUserAccessConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAccessConfiguration userAccessConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAccessConfiguration partially : {}, {}", id, userAccessConfiguration);
        if (userAccessConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccessConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccessConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAccessConfiguration> result = userAccessConfigurationRepository
            .findById(userAccessConfiguration.getId())
            .map(existingUserAccessConfiguration -> {
                if (userAccessConfiguration.getUserConfigId() != null) {
                    existingUserAccessConfiguration.setUserConfigId(userAccessConfiguration.getUserConfigId());
                }
                if (userAccessConfiguration.getUserId() != null) {
                    existingUserAccessConfiguration.setUserId(userAccessConfiguration.getUserId());
                }
                if (userAccessConfiguration.getAppId() != null) {
                    existingUserAccessConfiguration.setAppId(userAccessConfiguration.getAppId());
                }
                if (userAccessConfiguration.getActionType() != null) {
                    existingUserAccessConfiguration.setActionType(userAccessConfiguration.getActionType());
                }

                return existingUserAccessConfiguration;
            })
            .map(userAccessConfigurationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccessConfiguration.getId().toString())
        );
    }

    /**
     * {@code GET  /user-access-configurations} : get all the userAccessConfigurations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccessConfigurations in body.
     */
    @GetMapping("/user-access-configurations")
    public List<UserAccessConfiguration> getAllUserAccessConfigurations() {
        log.debug("REST request to get all UserAccessConfigurations");
        return userAccessConfigurationRepository.findAll();
    }

    /**
     * {@code GET  /user-access-configurations/:id} : get the "id" userAccessConfiguration.
     *
     * @param id the id of the userAccessConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccessConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-access-configurations/{id}")
    public ResponseEntity<UserAccessConfiguration> getUserAccessConfiguration(@PathVariable Long id) {
        log.debug("REST request to get UserAccessConfiguration : {}", id);
        Optional<UserAccessConfiguration> userAccessConfiguration = userAccessConfigurationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userAccessConfiguration);
    }

    /**
     * {@code DELETE  /user-access-configurations/:id} : delete the "id" userAccessConfiguration.
     *
     * @param id the id of the userAccessConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-access-configurations/{id}")
    public ResponseEntity<Void> deleteUserAccessConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete UserAccessConfiguration : {}", id);
        userAccessConfigurationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
