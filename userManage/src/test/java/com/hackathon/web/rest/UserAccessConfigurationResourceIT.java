package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.UserAccessConfiguration;
import com.hackathon.domain.enumeration.ActionType;
import com.hackathon.repository.UserAccessConfigurationRepository;
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
 * Integration tests for the {@link UserAccessConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAccessConfigurationResourceIT {

    private static final Long DEFAULT_USER_CONFIG_ID = 1L;
    private static final Long UPDATED_USER_CONFIG_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_APP_ID = 1L;
    private static final Long UPDATED_APP_ID = 2L;

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.DENY;
    private static final ActionType UPDATED_ACTION_TYPE = ActionType.VIEW;

    private static final String ENTITY_API_URL = "/api/user-access-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAccessConfigurationRepository userAccessConfigurationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAccessConfigurationMockMvc;

    private UserAccessConfiguration userAccessConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccessConfiguration createEntity(EntityManager em) {
        UserAccessConfiguration userAccessConfiguration = new UserAccessConfiguration()
            .userConfigId(DEFAULT_USER_CONFIG_ID)
            .userId(DEFAULT_USER_ID)
            .appId(DEFAULT_APP_ID)
            .actionType(DEFAULT_ACTION_TYPE);
        return userAccessConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccessConfiguration createUpdatedEntity(EntityManager em) {
        UserAccessConfiguration userAccessConfiguration = new UserAccessConfiguration()
            .userConfigId(UPDATED_USER_CONFIG_ID)
            .userId(UPDATED_USER_ID)
            .appId(UPDATED_APP_ID)
            .actionType(UPDATED_ACTION_TYPE);
        return userAccessConfiguration;
    }

    @BeforeEach
    public void initTest() {
        userAccessConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeCreate = userAccessConfigurationRepository.findAll().size();
        // Create the UserAccessConfiguration
        restUserAccessConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isCreated());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        UserAccessConfiguration testUserAccessConfiguration = userAccessConfigurationList.get(userAccessConfigurationList.size() - 1);
        assertThat(testUserAccessConfiguration.getUserConfigId()).isEqualTo(DEFAULT_USER_CONFIG_ID);
        assertThat(testUserAccessConfiguration.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserAccessConfiguration.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testUserAccessConfiguration.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
    }

    @Test
    @Transactional
    void createUserAccessConfigurationWithExistingId() throws Exception {
        // Create the UserAccessConfiguration with an existing ID
        userAccessConfiguration.setId(1L);

        int databaseSizeBeforeCreate = userAccessConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccessConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAccessConfigurations() throws Exception {
        // Initialize the database
        userAccessConfigurationRepository.saveAndFlush(userAccessConfiguration);

        // Get all the userAccessConfigurationList
        restUserAccessConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccessConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].userConfigId").value(hasItem(DEFAULT_USER_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID.intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getUserAccessConfiguration() throws Exception {
        // Initialize the database
        userAccessConfigurationRepository.saveAndFlush(userAccessConfiguration);

        // Get the userAccessConfiguration
        restUserAccessConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, userAccessConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccessConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.userConfigId").value(DEFAULT_USER_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.appId").value(DEFAULT_APP_ID.intValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserAccessConfiguration() throws Exception {
        // Get the userAccessConfiguration
        restUserAccessConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAccessConfiguration() throws Exception {
        // Initialize the database
        userAccessConfigurationRepository.saveAndFlush(userAccessConfiguration);

        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();

        // Update the userAccessConfiguration
        UserAccessConfiguration updatedUserAccessConfiguration = userAccessConfigurationRepository
            .findById(userAccessConfiguration.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserAccessConfiguration are not directly saved in db
        em.detach(updatedUserAccessConfiguration);
        updatedUserAccessConfiguration
            .userConfigId(UPDATED_USER_CONFIG_ID)
            .userId(UPDATED_USER_ID)
            .appId(UPDATED_APP_ID)
            .actionType(UPDATED_ACTION_TYPE);

        restUserAccessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserAccessConfiguration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserAccessConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
        UserAccessConfiguration testUserAccessConfiguration = userAccessConfigurationList.get(userAccessConfigurationList.size() - 1);
        assertThat(testUserAccessConfiguration.getUserConfigId()).isEqualTo(UPDATED_USER_CONFIG_ID);
        assertThat(testUserAccessConfiguration.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserAccessConfiguration.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testUserAccessConfiguration.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();
        userAccessConfiguration.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccessConfiguration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();
        userAccessConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();
        userAccessConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAccessConfigurationWithPatch() throws Exception {
        // Initialize the database
        userAccessConfigurationRepository.saveAndFlush(userAccessConfiguration);

        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();

        // Update the userAccessConfiguration using partial update
        UserAccessConfiguration partialUpdatedUserAccessConfiguration = new UserAccessConfiguration();
        partialUpdatedUserAccessConfiguration.setId(userAccessConfiguration.getId());

        restUserAccessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccessConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccessConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
        UserAccessConfiguration testUserAccessConfiguration = userAccessConfigurationList.get(userAccessConfigurationList.size() - 1);
        assertThat(testUserAccessConfiguration.getUserConfigId()).isEqualTo(DEFAULT_USER_CONFIG_ID);
        assertThat(testUserAccessConfiguration.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserAccessConfiguration.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testUserAccessConfiguration.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateUserAccessConfigurationWithPatch() throws Exception {
        // Initialize the database
        userAccessConfigurationRepository.saveAndFlush(userAccessConfiguration);

        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();

        // Update the userAccessConfiguration using partial update
        UserAccessConfiguration partialUpdatedUserAccessConfiguration = new UserAccessConfiguration();
        partialUpdatedUserAccessConfiguration.setId(userAccessConfiguration.getId());

        partialUpdatedUserAccessConfiguration
            .userConfigId(UPDATED_USER_CONFIG_ID)
            .userId(UPDATED_USER_ID)
            .appId(UPDATED_APP_ID)
            .actionType(UPDATED_ACTION_TYPE);

        restUserAccessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccessConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAccessConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
        UserAccessConfiguration testUserAccessConfiguration = userAccessConfigurationList.get(userAccessConfigurationList.size() - 1);
        assertThat(testUserAccessConfiguration.getUserConfigId()).isEqualTo(UPDATED_USER_CONFIG_ID);
        assertThat(testUserAccessConfiguration.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserAccessConfiguration.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testUserAccessConfiguration.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();
        userAccessConfiguration.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAccessConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();
        userAccessConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAccessConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = userAccessConfigurationRepository.findAll().size();
        userAccessConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAccessConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccessConfiguration in the database
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAccessConfiguration() throws Exception {
        // Initialize the database
        userAccessConfigurationRepository.saveAndFlush(userAccessConfiguration);

        int databaseSizeBeforeDelete = userAccessConfigurationRepository.findAll().size();

        // Delete the userAccessConfiguration
        restUserAccessConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAccessConfiguration.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAccessConfiguration> userAccessConfigurationList = userAccessConfigurationRepository.findAll();
        assertThat(userAccessConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
