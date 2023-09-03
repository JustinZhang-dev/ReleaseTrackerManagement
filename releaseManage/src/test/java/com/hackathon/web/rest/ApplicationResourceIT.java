package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.Application;
import com.hackathon.repository.ApplicationRepository;
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
 * Integration tests for the {@link ApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationResourceIT {

    private static final Long DEFAULT_APP_ID = 1L;
    private static final Long UPDATED_APP_ID = 2L;

    private static final String DEFAULT_APP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_APP_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_APP_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_APP_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_APP_MANAGER = "BBBBBBBBBB";

    private static final String DEFAULT_ORG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCTION_SUPPORT_DL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTION_SUPPORT_DL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationMockMvc;

    private Application application;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .appId(DEFAULT_APP_ID)
            .appName(DEFAULT_APP_NAME)
            .acronym(DEFAULT_ACRONYM)
            .appType(DEFAULT_APP_TYPE)
            .appManager(DEFAULT_APP_MANAGER)
            .orgName(DEFAULT_ORG_NAME)
            .teamEmail(DEFAULT_TEAM_EMAIL)
            .teamLocation(DEFAULT_TEAM_LOCATION)
            .productionSupportDl(DEFAULT_PRODUCTION_SUPPORT_DL);
        return application;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .appId(UPDATED_APP_ID)
            .appName(UPDATED_APP_NAME)
            .acronym(UPDATED_ACRONYM)
            .appType(UPDATED_APP_TYPE)
            .appManager(UPDATED_APP_MANAGER)
            .orgName(UPDATED_ORG_NAME)
            .teamEmail(UPDATED_TEAM_EMAIL)
            .teamLocation(UPDATED_TEAM_LOCATION)
            .productionSupportDl(UPDATED_PRODUCTION_SUPPORT_DL);
        return application;
    }

    @BeforeEach
    public void initTest() {
        application = createEntity(em);
    }

    @Test
    @Transactional
    void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();
        // Create the Application
        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testApplication.getAppName()).isEqualTo(DEFAULT_APP_NAME);
        assertThat(testApplication.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testApplication.getAppType()).isEqualTo(DEFAULT_APP_TYPE);
        assertThat(testApplication.getAppManager()).isEqualTo(DEFAULT_APP_MANAGER);
        assertThat(testApplication.getOrgName()).isEqualTo(DEFAULT_ORG_NAME);
        assertThat(testApplication.getTeamEmail()).isEqualTo(DEFAULT_TEAM_EMAIL);
        assertThat(testApplication.getTeamLocation()).isEqualTo(DEFAULT_TEAM_LOCATION);
        assertThat(testApplication.getProductionSupportDl()).isEqualTo(DEFAULT_PRODUCTION_SUPPORT_DL);
    }

    @Test
    @Transactional
    void createApplicationWithExistingId() throws Exception {
        // Create the Application with an existing ID
        application.setId(1L);

        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applicationList
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID.intValue())))
            .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME)))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM)))
            .andExpect(jsonPath("$.[*].appType").value(hasItem(DEFAULT_APP_TYPE)))
            .andExpect(jsonPath("$.[*].appManager").value(hasItem(DEFAULT_APP_MANAGER)))
            .andExpect(jsonPath("$.[*].orgName").value(hasItem(DEFAULT_ORG_NAME)))
            .andExpect(jsonPath("$.[*].teamEmail").value(hasItem(DEFAULT_TEAM_EMAIL)))
            .andExpect(jsonPath("$.[*].teamLocation").value(hasItem(DEFAULT_TEAM_LOCATION)))
            .andExpect(jsonPath("$.[*].productionSupportDl").value(hasItem(DEFAULT_PRODUCTION_SUPPORT_DL)));
    }

    @Test
    @Transactional
    void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.appId").value(DEFAULT_APP_ID.intValue()))
            .andExpect(jsonPath("$.appName").value(DEFAULT_APP_NAME))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM))
            .andExpect(jsonPath("$.appType").value(DEFAULT_APP_TYPE))
            .andExpect(jsonPath("$.appManager").value(DEFAULT_APP_MANAGER))
            .andExpect(jsonPath("$.orgName").value(DEFAULT_ORG_NAME))
            .andExpect(jsonPath("$.teamEmail").value(DEFAULT_TEAM_EMAIL))
            .andExpect(jsonPath("$.teamLocation").value(DEFAULT_TEAM_LOCATION))
            .andExpect(jsonPath("$.productionSupportDl").value(DEFAULT_PRODUCTION_SUPPORT_DL));
    }

    @Test
    @Transactional
    void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplication are not directly saved in db
        em.detach(updatedApplication);
        updatedApplication
            .appId(UPDATED_APP_ID)
            .appName(UPDATED_APP_NAME)
            .acronym(UPDATED_ACRONYM)
            .appType(UPDATED_APP_TYPE)
            .appManager(UPDATED_APP_MANAGER)
            .orgName(UPDATED_ORG_NAME)
            .teamEmail(UPDATED_TEAM_EMAIL)
            .teamLocation(UPDATED_TEAM_LOCATION)
            .productionSupportDl(UPDATED_PRODUCTION_SUPPORT_DL);

        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplication.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testApplication.getAppName()).isEqualTo(UPDATED_APP_NAME);
        assertThat(testApplication.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testApplication.getAppType()).isEqualTo(UPDATED_APP_TYPE);
        assertThat(testApplication.getAppManager()).isEqualTo(UPDATED_APP_MANAGER);
        assertThat(testApplication.getOrgName()).isEqualTo(UPDATED_ORG_NAME);
        assertThat(testApplication.getTeamEmail()).isEqualTo(UPDATED_TEAM_EMAIL);
        assertThat(testApplication.getTeamLocation()).isEqualTo(UPDATED_TEAM_LOCATION);
        assertThat(testApplication.getProductionSupportDl()).isEqualTo(UPDATED_PRODUCTION_SUPPORT_DL);
    }

    @Test
    @Transactional
    void putNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, application.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .appName(UPDATED_APP_NAME)
            .acronym(UPDATED_ACRONYM)
            .orgName(UPDATED_ORG_NAME)
            .teamEmail(UPDATED_TEAM_EMAIL)
            .teamLocation(UPDATED_TEAM_LOCATION);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testApplication.getAppName()).isEqualTo(UPDATED_APP_NAME);
        assertThat(testApplication.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testApplication.getAppType()).isEqualTo(DEFAULT_APP_TYPE);
        assertThat(testApplication.getAppManager()).isEqualTo(DEFAULT_APP_MANAGER);
        assertThat(testApplication.getOrgName()).isEqualTo(UPDATED_ORG_NAME);
        assertThat(testApplication.getTeamEmail()).isEqualTo(UPDATED_TEAM_EMAIL);
        assertThat(testApplication.getTeamLocation()).isEqualTo(UPDATED_TEAM_LOCATION);
        assertThat(testApplication.getProductionSupportDl()).isEqualTo(DEFAULT_PRODUCTION_SUPPORT_DL);
    }

    @Test
    @Transactional
    void fullUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .appId(UPDATED_APP_ID)
            .appName(UPDATED_APP_NAME)
            .acronym(UPDATED_ACRONYM)
            .appType(UPDATED_APP_TYPE)
            .appManager(UPDATED_APP_MANAGER)
            .orgName(UPDATED_ORG_NAME)
            .teamEmail(UPDATED_TEAM_EMAIL)
            .teamLocation(UPDATED_TEAM_LOCATION)
            .productionSupportDl(UPDATED_PRODUCTION_SUPPORT_DL);

        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplication))
            )
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applicationList.get(applicationList.size() - 1);
        assertThat(testApplication.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testApplication.getAppName()).isEqualTo(UPDATED_APP_NAME);
        assertThat(testApplication.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testApplication.getAppType()).isEqualTo(UPDATED_APP_TYPE);
        assertThat(testApplication.getAppManager()).isEqualTo(UPDATED_APP_MANAGER);
        assertThat(testApplication.getOrgName()).isEqualTo(UPDATED_ORG_NAME);
        assertThat(testApplication.getTeamEmail()).isEqualTo(UPDATED_TEAM_EMAIL);
        assertThat(testApplication.getTeamLocation()).isEqualTo(UPDATED_TEAM_LOCATION);
        assertThat(testApplication.getProductionSupportDl()).isEqualTo(UPDATED_PRODUCTION_SUPPORT_DL);
    }

    @Test
    @Transactional
    void patchNonExistingApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, application.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isBadRequest());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplication() throws Exception {
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();
        application.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(application))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Application in the database
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Delete the application
        restApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, application.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Application> applicationList = applicationRepository.findAll();
        assertThat(applicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
