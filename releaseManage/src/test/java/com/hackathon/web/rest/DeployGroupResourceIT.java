package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.DeployGroup;
import com.hackathon.repository.DeployGroupRepository;
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
 * Integration tests for the {@link DeployGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeployGroupResourceIT {

    private static final Long DEFAULT_DEPLOY_GROUP_ID = 1L;
    private static final Long UPDATED_DEPLOY_GROUP_ID = 2L;

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_APP_ID = 1L;
    private static final Long UPDATED_APP_ID = 2L;

    private static final String DEFAULT_DEPLOY_MANAGER_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOY_MANAGER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deploy-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeployGroupRepository deployGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeployGroupMockMvc;

    private DeployGroup deployGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeployGroup createEntity(EntityManager em) {
        DeployGroup deployGroup = new DeployGroup()
            .deployGroupId(DEFAULT_DEPLOY_GROUP_ID)
            .groupName(DEFAULT_GROUP_NAME)
            .appId(DEFAULT_APP_ID)
            .deployManagerId(DEFAULT_DEPLOY_MANAGER_ID);
        return deployGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeployGroup createUpdatedEntity(EntityManager em) {
        DeployGroup deployGroup = new DeployGroup()
            .deployGroupId(UPDATED_DEPLOY_GROUP_ID)
            .groupName(UPDATED_GROUP_NAME)
            .appId(UPDATED_APP_ID)
            .deployManagerId(UPDATED_DEPLOY_MANAGER_ID);
        return deployGroup;
    }

    @BeforeEach
    public void initTest() {
        deployGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createDeployGroup() throws Exception {
        int databaseSizeBeforeCreate = deployGroupRepository.findAll().size();
        // Create the DeployGroup
        restDeployGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isCreated());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeCreate + 1);
        DeployGroup testDeployGroup = deployGroupList.get(deployGroupList.size() - 1);
        assertThat(testDeployGroup.getDeployGroupId()).isEqualTo(DEFAULT_DEPLOY_GROUP_ID);
        assertThat(testDeployGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testDeployGroup.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testDeployGroup.getDeployManagerId()).isEqualTo(DEFAULT_DEPLOY_MANAGER_ID);
    }

    @Test
    @Transactional
    void createDeployGroupWithExistingId() throws Exception {
        // Create the DeployGroup with an existing ID
        deployGroup.setId(1L);

        int databaseSizeBeforeCreate = deployGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeployGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeployGroups() throws Exception {
        // Initialize the database
        deployGroupRepository.saveAndFlush(deployGroup);

        // Get all the deployGroupList
        restDeployGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deployGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].deployGroupId").value(hasItem(DEFAULT_DEPLOY_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID.intValue())))
            .andExpect(jsonPath("$.[*].deployManagerId").value(hasItem(DEFAULT_DEPLOY_MANAGER_ID)));
    }

    @Test
    @Transactional
    void getDeployGroup() throws Exception {
        // Initialize the database
        deployGroupRepository.saveAndFlush(deployGroup);

        // Get the deployGroup
        restDeployGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, deployGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deployGroup.getId().intValue()))
            .andExpect(jsonPath("$.deployGroupId").value(DEFAULT_DEPLOY_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.appId").value(DEFAULT_APP_ID.intValue()))
            .andExpect(jsonPath("$.deployManagerId").value(DEFAULT_DEPLOY_MANAGER_ID));
    }

    @Test
    @Transactional
    void getNonExistingDeployGroup() throws Exception {
        // Get the deployGroup
        restDeployGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeployGroup() throws Exception {
        // Initialize the database
        deployGroupRepository.saveAndFlush(deployGroup);

        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();

        // Update the deployGroup
        DeployGroup updatedDeployGroup = deployGroupRepository.findById(deployGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeployGroup are not directly saved in db
        em.detach(updatedDeployGroup);
        updatedDeployGroup
            .deployGroupId(UPDATED_DEPLOY_GROUP_ID)
            .groupName(UPDATED_GROUP_NAME)
            .appId(UPDATED_APP_ID)
            .deployManagerId(UPDATED_DEPLOY_MANAGER_ID);

        restDeployGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeployGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeployGroup))
            )
            .andExpect(status().isOk());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
        DeployGroup testDeployGroup = deployGroupList.get(deployGroupList.size() - 1);
        assertThat(testDeployGroup.getDeployGroupId()).isEqualTo(UPDATED_DEPLOY_GROUP_ID);
        assertThat(testDeployGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testDeployGroup.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testDeployGroup.getDeployManagerId()).isEqualTo(UPDATED_DEPLOY_MANAGER_ID);
    }

    @Test
    @Transactional
    void putNonExistingDeployGroup() throws Exception {
        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();
        deployGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeployGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deployGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeployGroup() throws Exception {
        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();
        deployGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeployGroup() throws Exception {
        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();
        deployGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeployGroupWithPatch() throws Exception {
        // Initialize the database
        deployGroupRepository.saveAndFlush(deployGroup);

        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();

        // Update the deployGroup using partial update
        DeployGroup partialUpdatedDeployGroup = new DeployGroup();
        partialUpdatedDeployGroup.setId(deployGroup.getId());

        partialUpdatedDeployGroup.deployManagerId(UPDATED_DEPLOY_MANAGER_ID);

        restDeployGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeployGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeployGroup))
            )
            .andExpect(status().isOk());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
        DeployGroup testDeployGroup = deployGroupList.get(deployGroupList.size() - 1);
        assertThat(testDeployGroup.getDeployGroupId()).isEqualTo(DEFAULT_DEPLOY_GROUP_ID);
        assertThat(testDeployGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testDeployGroup.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testDeployGroup.getDeployManagerId()).isEqualTo(UPDATED_DEPLOY_MANAGER_ID);
    }

    @Test
    @Transactional
    void fullUpdateDeployGroupWithPatch() throws Exception {
        // Initialize the database
        deployGroupRepository.saveAndFlush(deployGroup);

        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();

        // Update the deployGroup using partial update
        DeployGroup partialUpdatedDeployGroup = new DeployGroup();
        partialUpdatedDeployGroup.setId(deployGroup.getId());

        partialUpdatedDeployGroup
            .deployGroupId(UPDATED_DEPLOY_GROUP_ID)
            .groupName(UPDATED_GROUP_NAME)
            .appId(UPDATED_APP_ID)
            .deployManagerId(UPDATED_DEPLOY_MANAGER_ID);

        restDeployGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeployGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeployGroup))
            )
            .andExpect(status().isOk());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
        DeployGroup testDeployGroup = deployGroupList.get(deployGroupList.size() - 1);
        assertThat(testDeployGroup.getDeployGroupId()).isEqualTo(UPDATED_DEPLOY_GROUP_ID);
        assertThat(testDeployGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testDeployGroup.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testDeployGroup.getDeployManagerId()).isEqualTo(UPDATED_DEPLOY_MANAGER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingDeployGroup() throws Exception {
        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();
        deployGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeployGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deployGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeployGroup() throws Exception {
        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();
        deployGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeployGroup() throws Exception {
        int databaseSizeBeforeUpdate = deployGroupRepository.findAll().size();
        deployGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeployGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deployGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeployGroup in the database
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeployGroup() throws Exception {
        // Initialize the database
        deployGroupRepository.saveAndFlush(deployGroup);

        int databaseSizeBeforeDelete = deployGroupRepository.findAll().size();

        // Delete the deployGroup
        restDeployGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, deployGroup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeployGroup> deployGroupList = deployGroupRepository.findAll();
        assertThat(deployGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
