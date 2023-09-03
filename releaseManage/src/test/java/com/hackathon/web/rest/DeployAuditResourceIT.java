package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.DeployAudit;
import com.hackathon.repository.DeployAuditRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeployAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeployAuditResourceIT {

    private static final Long DEFAULT_AUDIT_ID = 1L;
    private static final Long UPDATED_AUDIT_ID = 2L;

    private static final Long DEFAULT_DEPLOY_ID = 1L;
    private static final Long UPDATED_DEPLOY_ID = 2L;

    private static final String DEFAULT_DEPLOY_FORMER_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOY_FORMER_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOYED_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOYED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOYED_OPERATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOYED_OPERATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_PERFORMED_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_PERFORMED_ROLE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_PERFORMED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_PERFORMED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deploy-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeployAuditRepository deployAuditRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeployAuditMockMvc;

    private DeployAudit deployAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeployAudit createEntity(EntityManager em) {
        DeployAudit deployAudit = new DeployAudit()
            .auditId(DEFAULT_AUDIT_ID)
            .deployId(DEFAULT_DEPLOY_ID)
            .deployFormerStatus(DEFAULT_DEPLOY_FORMER_STATUS)
            .deployedStatus(DEFAULT_DEPLOYED_STATUS)
            .deployedOperationType(DEFAULT_DEPLOYED_OPERATION_TYPE)
            .actionPerformedRole(DEFAULT_ACTION_PERFORMED_ROLE)
            .actionPerformedBy(DEFAULT_ACTION_PERFORMED_BY);
        return deployAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeployAudit createUpdatedEntity(EntityManager em) {
        DeployAudit deployAudit = new DeployAudit()
            .auditId(UPDATED_AUDIT_ID)
            .deployId(UPDATED_DEPLOY_ID)
            .deployFormerStatus(UPDATED_DEPLOY_FORMER_STATUS)
            .deployedStatus(UPDATED_DEPLOYED_STATUS)
            .deployedOperationType(UPDATED_DEPLOYED_OPERATION_TYPE)
            .actionPerformedRole(UPDATED_ACTION_PERFORMED_ROLE)
            .actionPerformedBy(UPDATED_ACTION_PERFORMED_BY);
        return deployAudit;
    }

    @BeforeEach
    public void initTest() {
        deployAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createDeployAudit() throws Exception {
        int databaseSizeBeforeCreate = deployAuditRepository.findAll().size();
        // Create the DeployAudit
        restDeployAuditMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isCreated());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeCreate + 1);
        DeployAudit testDeployAudit = deployAuditList.get(deployAuditList.size() - 1);
        assertThat(testDeployAudit.getAuditId()).isEqualTo(DEFAULT_AUDIT_ID);
        assertThat(testDeployAudit.getDeployId()).isEqualTo(DEFAULT_DEPLOY_ID);
        assertThat(testDeployAudit.getDeployFormerStatus()).isEqualTo(DEFAULT_DEPLOY_FORMER_STATUS);
        assertThat(testDeployAudit.getDeployedStatus()).isEqualTo(DEFAULT_DEPLOYED_STATUS);
        assertThat(testDeployAudit.getDeployedOperationType()).isEqualTo(DEFAULT_DEPLOYED_OPERATION_TYPE);
        assertThat(testDeployAudit.getActionPerformedRole()).isEqualTo(DEFAULT_ACTION_PERFORMED_ROLE);
        assertThat(testDeployAudit.getActionPerformedBy()).isEqualTo(DEFAULT_ACTION_PERFORMED_BY);
    }

    @Test
    @Transactional
    void createDeployAuditWithExistingId() throws Exception {
        // Create the DeployAudit with an existing ID
        deployAudit.setId(1L);

        int databaseSizeBeforeCreate = deployAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeployAuditMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeployAudits() throws Exception {
        // Initialize the database
        deployAuditRepository.saveAndFlush(deployAudit);

        // Get all the deployAuditList
        restDeployAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deployAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].deployId").value(hasItem(DEFAULT_DEPLOY_ID.intValue())))
            .andExpect(jsonPath("$.[*].deployFormerStatus").value(hasItem(DEFAULT_DEPLOY_FORMER_STATUS)))
            .andExpect(jsonPath("$.[*].deployedStatus").value(hasItem(DEFAULT_DEPLOYED_STATUS)))
            .andExpect(jsonPath("$.[*].deployedOperationType").value(hasItem(DEFAULT_DEPLOYED_OPERATION_TYPE)))
            .andExpect(jsonPath("$.[*].actionPerformedRole").value(hasItem(DEFAULT_ACTION_PERFORMED_ROLE)))
            .andExpect(jsonPath("$.[*].actionPerformedBy").value(hasItem(DEFAULT_ACTION_PERFORMED_BY)));
    }

    @Test
    @Transactional
    void getDeployAudit() throws Exception {
        // Initialize the database
        deployAuditRepository.saveAndFlush(deployAudit);

        // Get the deployAudit
        restDeployAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, deployAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deployAudit.getId().intValue()))
            .andExpect(jsonPath("$.auditId").value(DEFAULT_AUDIT_ID.intValue()))
            .andExpect(jsonPath("$.deployId").value(DEFAULT_DEPLOY_ID.intValue()))
            .andExpect(jsonPath("$.deployFormerStatus").value(DEFAULT_DEPLOY_FORMER_STATUS))
            .andExpect(jsonPath("$.deployedStatus").value(DEFAULT_DEPLOYED_STATUS))
            .andExpect(jsonPath("$.deployedOperationType").value(DEFAULT_DEPLOYED_OPERATION_TYPE))
            .andExpect(jsonPath("$.actionPerformedRole").value(DEFAULT_ACTION_PERFORMED_ROLE))
            .andExpect(jsonPath("$.actionPerformedBy").value(DEFAULT_ACTION_PERFORMED_BY));
    }

    @Test
    @Transactional
    void getNonExistingDeployAudit() throws Exception {
        // Get the deployAudit
        restDeployAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeployAudit() throws Exception {
        // Initialize the database
        deployAuditRepository.saveAndFlush(deployAudit);

        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();

        // Update the deployAudit
        DeployAudit updatedDeployAudit = deployAuditRepository.findById(deployAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeployAudit are not directly saved in db
        em.detach(updatedDeployAudit);
        updatedDeployAudit
            .auditId(UPDATED_AUDIT_ID)
            .deployId(UPDATED_DEPLOY_ID)
            .deployFormerStatus(UPDATED_DEPLOY_FORMER_STATUS)
            .deployedStatus(UPDATED_DEPLOYED_STATUS)
            .deployedOperationType(UPDATED_DEPLOYED_OPERATION_TYPE)
            .actionPerformedRole(UPDATED_ACTION_PERFORMED_ROLE)
            .actionPerformedBy(UPDATED_ACTION_PERFORMED_BY);

        restDeployAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeployAudit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeployAudit))
            )
            .andExpect(status().isOk());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
        DeployAudit testDeployAudit = deployAuditList.get(deployAuditList.size() - 1);
        assertThat(testDeployAudit.getAuditId()).isEqualTo(UPDATED_AUDIT_ID);
        assertThat(testDeployAudit.getDeployId()).isEqualTo(UPDATED_DEPLOY_ID);
        assertThat(testDeployAudit.getDeployFormerStatus()).isEqualTo(UPDATED_DEPLOY_FORMER_STATUS);
        assertThat(testDeployAudit.getDeployedStatus()).isEqualTo(UPDATED_DEPLOYED_STATUS);
        assertThat(testDeployAudit.getDeployedOperationType()).isEqualTo(UPDATED_DEPLOYED_OPERATION_TYPE);
        assertThat(testDeployAudit.getActionPerformedRole()).isEqualTo(UPDATED_ACTION_PERFORMED_ROLE);
        assertThat(testDeployAudit.getActionPerformedBy()).isEqualTo(UPDATED_ACTION_PERFORMED_BY);
    }

    @Test
    @Transactional
    void putNonExistingDeployAudit() throws Exception {
        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();
        deployAudit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeployAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deployAudit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeployAudit() throws Exception {
        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();
        deployAudit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeployAudit() throws Exception {
        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();
        deployAudit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployAuditMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeployAuditWithPatch() throws Exception {
        // Initialize the database
        deployAuditRepository.saveAndFlush(deployAudit);

        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();

        // Update the deployAudit using partial update
        DeployAudit partialUpdatedDeployAudit = new DeployAudit();
        partialUpdatedDeployAudit.setId(deployAudit.getId());

        partialUpdatedDeployAudit
            .deployFormerStatus(UPDATED_DEPLOY_FORMER_STATUS)
            .deployedStatus(UPDATED_DEPLOYED_STATUS)
            .actionPerformedBy(UPDATED_ACTION_PERFORMED_BY);

        restDeployAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeployAudit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeployAudit))
            )
            .andExpect(status().isOk());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
        DeployAudit testDeployAudit = deployAuditList.get(deployAuditList.size() - 1);
        assertThat(testDeployAudit.getAuditId()).isEqualTo(DEFAULT_AUDIT_ID);
        assertThat(testDeployAudit.getDeployId()).isEqualTo(DEFAULT_DEPLOY_ID);
        assertThat(testDeployAudit.getDeployFormerStatus()).isEqualTo(UPDATED_DEPLOY_FORMER_STATUS);
        assertThat(testDeployAudit.getDeployedStatus()).isEqualTo(UPDATED_DEPLOYED_STATUS);
        assertThat(testDeployAudit.getDeployedOperationType()).isEqualTo(DEFAULT_DEPLOYED_OPERATION_TYPE);
        assertThat(testDeployAudit.getActionPerformedRole()).isEqualTo(DEFAULT_ACTION_PERFORMED_ROLE);
        assertThat(testDeployAudit.getActionPerformedBy()).isEqualTo(UPDATED_ACTION_PERFORMED_BY);
    }

    @Test
    @Transactional
    void fullUpdateDeployAuditWithPatch() throws Exception {
        // Initialize the database
        deployAuditRepository.saveAndFlush(deployAudit);

        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();

        // Update the deployAudit using partial update
        DeployAudit partialUpdatedDeployAudit = new DeployAudit();
        partialUpdatedDeployAudit.setId(deployAudit.getId());

        partialUpdatedDeployAudit
            .auditId(UPDATED_AUDIT_ID)
            .deployId(UPDATED_DEPLOY_ID)
            .deployFormerStatus(UPDATED_DEPLOY_FORMER_STATUS)
            .deployedStatus(UPDATED_DEPLOYED_STATUS)
            .deployedOperationType(UPDATED_DEPLOYED_OPERATION_TYPE)
            .actionPerformedRole(UPDATED_ACTION_PERFORMED_ROLE)
            .actionPerformedBy(UPDATED_ACTION_PERFORMED_BY);

        restDeployAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeployAudit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeployAudit))
            )
            .andExpect(status().isOk());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
        DeployAudit testDeployAudit = deployAuditList.get(deployAuditList.size() - 1);
        assertThat(testDeployAudit.getAuditId()).isEqualTo(UPDATED_AUDIT_ID);
        assertThat(testDeployAudit.getDeployId()).isEqualTo(UPDATED_DEPLOY_ID);
        assertThat(testDeployAudit.getDeployFormerStatus()).isEqualTo(UPDATED_DEPLOY_FORMER_STATUS);
        assertThat(testDeployAudit.getDeployedStatus()).isEqualTo(UPDATED_DEPLOYED_STATUS);
        assertThat(testDeployAudit.getDeployedOperationType()).isEqualTo(UPDATED_DEPLOYED_OPERATION_TYPE);
        assertThat(testDeployAudit.getActionPerformedRole()).isEqualTo(UPDATED_ACTION_PERFORMED_ROLE);
        assertThat(testDeployAudit.getActionPerformedBy()).isEqualTo(UPDATED_ACTION_PERFORMED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingDeployAudit() throws Exception {
        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();
        deployAudit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeployAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deployAudit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeployAudit() throws Exception {
        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();
        deployAudit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeployAudit() throws Exception {
        int databaseSizeBeforeUpdate = deployAuditRepository.findAll().size();
        deployAudit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployAuditMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deployAudit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeployAudit in the database
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeployAudit() throws Exception {
        // Initialize the database
        deployAuditRepository.saveAndFlush(deployAudit);

        int databaseSizeBeforeDelete = deployAuditRepository.findAll().size();

        // Delete the deployAudit
        restDeployAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, deployAudit.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeployAudit> deployAuditList = deployAuditRepository.findAll();
        assertThat(deployAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
