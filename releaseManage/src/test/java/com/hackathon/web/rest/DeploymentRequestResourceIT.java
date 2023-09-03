package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.DeploymentRequest;
import com.hackathon.repository.DeploymentRequestRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DeploymentRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeploymentRequestResourceIT {

    private static final Long DEFAULT_DEPLOY_ID = 1L;
    private static final Long UPDATED_DEPLOY_ID = 2L;

    private static final Long DEFAULT_DEPLOY_GROUP_ID = 1L;
    private static final Long UPDATED_DEPLOY_GROUP_ID = 2L;

    private static final Integer DEFAULT_DEPLOY_SEQUENCE = 1;
    private static final Integer UPDATED_DEPLOY_SEQUENCE = 2;

    private static final Long DEFAULT_APP_ID = 1L;
    private static final Long UPDATED_APP_ID = 2L;

    private static final String DEFAULT_DEPLOY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_COMPONENT = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_COMPONENT = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOY_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOY_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PORTAL = "AAAAAAAAAA";
    private static final String UPDATED_PORTAL = "BBBBBBBBBB";

    private static final String DEFAULT_REQUESTOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_JIRA_ID = "AAAAAAAAAA";
    private static final String UPDATED_JIRA_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_PTS_ID = 1L;
    private static final Long UPDATED_PTS_ID = 2L;

    private static final String DEFAULT_BUILD_ID = "AAAAAAAAAA";
    private static final String UPDATED_BUILD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RLM_REQUEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_RLM_REQUEST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RLM_REQUEST_ENV = "AAAAAAAAAA";
    private static final String UPDATED_RLM_REQUEST_ENV = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOYMENT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOYMENT_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_PROMOTION_ENV = "AAAAAAAAAA";
    private static final String UPDATED_PROMOTION_ENV = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DEPLOY_COMPLETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEPLOY_COMPLETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_TRIGGER_NEXT_SEQ_DEPLOY = false;
    private static final Boolean UPDATED_TRIGGER_NEXT_SEQ_DEPLOY = true;

    private static final Long DEFAULT_QA_SIGN_OFF_ID = 1L;
    private static final Long UPDATED_QA_SIGN_OFF_ID = 2L;

    private static final String DEFAULT_CHGNO = "AAAAAAAAAA";
    private static final String UPDATED_CHGNO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deployment-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeploymentRequestRepository deploymentRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeploymentRequestMockMvc;

    private DeploymentRequest deploymentRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeploymentRequest createEntity(EntityManager em) {
        DeploymentRequest deploymentRequest = new DeploymentRequest()
            .deployId(DEFAULT_DEPLOY_ID)
            .deployGroupId(DEFAULT_DEPLOY_GROUP_ID)
            .deploySequence(DEFAULT_DEPLOY_SEQUENCE)
            .appId(DEFAULT_APP_ID)
            .deployName(DEFAULT_DEPLOY_NAME)
            .requestComponent(DEFAULT_REQUEST_COMPONENT)
            .deployStatus(DEFAULT_DEPLOY_STATUS)
            .requestType(DEFAULT_REQUEST_TYPE)
            .releaseType(DEFAULT_RELEASE_TYPE)
            .moduleName(DEFAULT_MODULE_NAME)
            .portal(DEFAULT_PORTAL)
            .requestorID(DEFAULT_REQUESTOR_ID)
            .jiraId(DEFAULT_JIRA_ID)
            .ptsId(DEFAULT_PTS_ID)
            .buildId(DEFAULT_BUILD_ID)
            .rlmRequestId(DEFAULT_RLM_REQUEST_ID)
            .rlmRequestEnv(DEFAULT_RLM_REQUEST_ENV)
            .deploymentCategory(DEFAULT_DEPLOYMENT_CATEGORY)
            .promotionEnv(DEFAULT_PROMOTION_ENV)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .deployCompletedDate(DEFAULT_DEPLOY_COMPLETED_DATE)
            .triggerNextSeqDeploy(DEFAULT_TRIGGER_NEXT_SEQ_DEPLOY)
            .qaSignOffId(DEFAULT_QA_SIGN_OFF_ID)
            .chgno(DEFAULT_CHGNO);
        return deploymentRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeploymentRequest createUpdatedEntity(EntityManager em) {
        DeploymentRequest deploymentRequest = new DeploymentRequest()
            .deployId(UPDATED_DEPLOY_ID)
            .deployGroupId(UPDATED_DEPLOY_GROUP_ID)
            .deploySequence(UPDATED_DEPLOY_SEQUENCE)
            .appId(UPDATED_APP_ID)
            .deployName(UPDATED_DEPLOY_NAME)
            .requestComponent(UPDATED_REQUEST_COMPONENT)
            .deployStatus(UPDATED_DEPLOY_STATUS)
            .requestType(UPDATED_REQUEST_TYPE)
            .releaseType(UPDATED_RELEASE_TYPE)
            .moduleName(UPDATED_MODULE_NAME)
            .portal(UPDATED_PORTAL)
            .requestorID(UPDATED_REQUESTOR_ID)
            .jiraId(UPDATED_JIRA_ID)
            .ptsId(UPDATED_PTS_ID)
            .buildId(UPDATED_BUILD_ID)
            .rlmRequestId(UPDATED_RLM_REQUEST_ID)
            .rlmRequestEnv(UPDATED_RLM_REQUEST_ENV)
            .deploymentCategory(UPDATED_DEPLOYMENT_CATEGORY)
            .promotionEnv(UPDATED_PROMOTION_ENV)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .deployCompletedDate(UPDATED_DEPLOY_COMPLETED_DATE)
            .triggerNextSeqDeploy(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY)
            .qaSignOffId(UPDATED_QA_SIGN_OFF_ID)
            .chgno(UPDATED_CHGNO);
        return deploymentRequest;
    }

    @BeforeEach
    public void initTest() {
        deploymentRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createDeploymentRequest() throws Exception {
        int databaseSizeBeforeCreate = deploymentRequestRepository.findAll().size();
        // Create the DeploymentRequest
        restDeploymentRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isCreated());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeCreate + 1);
        DeploymentRequest testDeploymentRequest = deploymentRequestList.get(deploymentRequestList.size() - 1);
        assertThat(testDeploymentRequest.getDeployId()).isEqualTo(DEFAULT_DEPLOY_ID);
        assertThat(testDeploymentRequest.getDeployGroupId()).isEqualTo(DEFAULT_DEPLOY_GROUP_ID);
        assertThat(testDeploymentRequest.getDeploySequence()).isEqualTo(DEFAULT_DEPLOY_SEQUENCE);
        assertThat(testDeploymentRequest.getAppId()).isEqualTo(DEFAULT_APP_ID);
        assertThat(testDeploymentRequest.getDeployName()).isEqualTo(DEFAULT_DEPLOY_NAME);
        assertThat(testDeploymentRequest.getRequestComponent()).isEqualTo(DEFAULT_REQUEST_COMPONENT);
        assertThat(testDeploymentRequest.getDeployStatus()).isEqualTo(DEFAULT_DEPLOY_STATUS);
        assertThat(testDeploymentRequest.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testDeploymentRequest.getReleaseType()).isEqualTo(DEFAULT_RELEASE_TYPE);
        assertThat(testDeploymentRequest.getModuleName()).isEqualTo(DEFAULT_MODULE_NAME);
        assertThat(testDeploymentRequest.getPortal()).isEqualTo(DEFAULT_PORTAL);
        assertThat(testDeploymentRequest.getRequestorID()).isEqualTo(DEFAULT_REQUESTOR_ID);
        assertThat(testDeploymentRequest.getJiraId()).isEqualTo(DEFAULT_JIRA_ID);
        assertThat(testDeploymentRequest.getPtsId()).isEqualTo(DEFAULT_PTS_ID);
        assertThat(testDeploymentRequest.getBuildId()).isEqualTo(DEFAULT_BUILD_ID);
        assertThat(testDeploymentRequest.getRlmRequestId()).isEqualTo(DEFAULT_RLM_REQUEST_ID);
        assertThat(testDeploymentRequest.getRlmRequestEnv()).isEqualTo(DEFAULT_RLM_REQUEST_ENV);
        assertThat(testDeploymentRequest.getDeploymentCategory()).isEqualTo(DEFAULT_DEPLOYMENT_CATEGORY);
        assertThat(testDeploymentRequest.getPromotionEnv()).isEqualTo(DEFAULT_PROMOTION_ENV);
        assertThat(testDeploymentRequest.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testDeploymentRequest.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testDeploymentRequest.getDeployCompletedDate()).isEqualTo(DEFAULT_DEPLOY_COMPLETED_DATE);
        assertThat(testDeploymentRequest.getTriggerNextSeqDeploy()).isEqualTo(DEFAULT_TRIGGER_NEXT_SEQ_DEPLOY);
        assertThat(testDeploymentRequest.getQaSignOffId()).isEqualTo(DEFAULT_QA_SIGN_OFF_ID);
        assertThat(testDeploymentRequest.getChgno()).isEqualTo(DEFAULT_CHGNO);
    }

    @Test
    @Transactional
    void createDeploymentRequestWithExistingId() throws Exception {
        // Create the DeploymentRequest with an existing ID
        deploymentRequest.setId(1L);

        int databaseSizeBeforeCreate = deploymentRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeploymentRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeploymentRequests() throws Exception {
        // Initialize the database
        deploymentRequestRepository.saveAndFlush(deploymentRequest);

        // Get all the deploymentRequestList
        restDeploymentRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deploymentRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].deployId").value(hasItem(DEFAULT_DEPLOY_ID.intValue())))
            .andExpect(jsonPath("$.[*].deployGroupId").value(hasItem(DEFAULT_DEPLOY_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].deploySequence").value(hasItem(DEFAULT_DEPLOY_SEQUENCE)))
            .andExpect(jsonPath("$.[*].appId").value(hasItem(DEFAULT_APP_ID.intValue())))
            .andExpect(jsonPath("$.[*].deployName").value(hasItem(DEFAULT_DEPLOY_NAME)))
            .andExpect(jsonPath("$.[*].requestComponent").value(hasItem(DEFAULT_REQUEST_COMPONENT)))
            .andExpect(jsonPath("$.[*].deployStatus").value(hasItem(DEFAULT_DEPLOY_STATUS)))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].releaseType").value(hasItem(DEFAULT_RELEASE_TYPE)))
            .andExpect(jsonPath("$.[*].moduleName").value(hasItem(DEFAULT_MODULE_NAME)))
            .andExpect(jsonPath("$.[*].portal").value(hasItem(DEFAULT_PORTAL)))
            .andExpect(jsonPath("$.[*].requestorID").value(hasItem(DEFAULT_REQUESTOR_ID)))
            .andExpect(jsonPath("$.[*].jiraId").value(hasItem(DEFAULT_JIRA_ID)))
            .andExpect(jsonPath("$.[*].ptsId").value(hasItem(DEFAULT_PTS_ID.intValue())))
            .andExpect(jsonPath("$.[*].buildId").value(hasItem(DEFAULT_BUILD_ID)))
            .andExpect(jsonPath("$.[*].rlmRequestId").value(hasItem(DEFAULT_RLM_REQUEST_ID)))
            .andExpect(jsonPath("$.[*].rlmRequestEnv").value(hasItem(DEFAULT_RLM_REQUEST_ENV)))
            .andExpect(jsonPath("$.[*].deploymentCategory").value(hasItem(DEFAULT_DEPLOYMENT_CATEGORY)))
            .andExpect(jsonPath("$.[*].promotionEnv").value(hasItem(DEFAULT_PROMOTION_ENV)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].deployCompletedDate").value(hasItem(DEFAULT_DEPLOY_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].triggerNextSeqDeploy").value(hasItem(DEFAULT_TRIGGER_NEXT_SEQ_DEPLOY.booleanValue())))
            .andExpect(jsonPath("$.[*].qaSignOffId").value(hasItem(DEFAULT_QA_SIGN_OFF_ID.intValue())))
            .andExpect(jsonPath("$.[*].chgno").value(hasItem(DEFAULT_CHGNO)));
    }

    @Test
    @Transactional
    void getDeploymentRequest() throws Exception {
        // Initialize the database
        deploymentRequestRepository.saveAndFlush(deploymentRequest);

        // Get the deploymentRequest
        restDeploymentRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, deploymentRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deploymentRequest.getId().intValue()))
            .andExpect(jsonPath("$.deployId").value(DEFAULT_DEPLOY_ID.intValue()))
            .andExpect(jsonPath("$.deployGroupId").value(DEFAULT_DEPLOY_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.deploySequence").value(DEFAULT_DEPLOY_SEQUENCE))
            .andExpect(jsonPath("$.appId").value(DEFAULT_APP_ID.intValue()))
            .andExpect(jsonPath("$.deployName").value(DEFAULT_DEPLOY_NAME))
            .andExpect(jsonPath("$.requestComponent").value(DEFAULT_REQUEST_COMPONENT))
            .andExpect(jsonPath("$.deployStatus").value(DEFAULT_DEPLOY_STATUS))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE))
            .andExpect(jsonPath("$.releaseType").value(DEFAULT_RELEASE_TYPE))
            .andExpect(jsonPath("$.moduleName").value(DEFAULT_MODULE_NAME))
            .andExpect(jsonPath("$.portal").value(DEFAULT_PORTAL))
            .andExpect(jsonPath("$.requestorID").value(DEFAULT_REQUESTOR_ID))
            .andExpect(jsonPath("$.jiraId").value(DEFAULT_JIRA_ID))
            .andExpect(jsonPath("$.ptsId").value(DEFAULT_PTS_ID.intValue()))
            .andExpect(jsonPath("$.buildId").value(DEFAULT_BUILD_ID))
            .andExpect(jsonPath("$.rlmRequestId").value(DEFAULT_RLM_REQUEST_ID))
            .andExpect(jsonPath("$.rlmRequestEnv").value(DEFAULT_RLM_REQUEST_ENV))
            .andExpect(jsonPath("$.deploymentCategory").value(DEFAULT_DEPLOYMENT_CATEGORY))
            .andExpect(jsonPath("$.promotionEnv").value(DEFAULT_PROMOTION_ENV))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.deployCompletedDate").value(DEFAULT_DEPLOY_COMPLETED_DATE.toString()))
            .andExpect(jsonPath("$.triggerNextSeqDeploy").value(DEFAULT_TRIGGER_NEXT_SEQ_DEPLOY.booleanValue()))
            .andExpect(jsonPath("$.qaSignOffId").value(DEFAULT_QA_SIGN_OFF_ID.intValue()))
            .andExpect(jsonPath("$.chgno").value(DEFAULT_CHGNO));
    }

    @Test
    @Transactional
    void getNonExistingDeploymentRequest() throws Exception {
        // Get the deploymentRequest
        restDeploymentRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeploymentRequest() throws Exception {
        // Initialize the database
        deploymentRequestRepository.saveAndFlush(deploymentRequest);

        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();

        // Update the deploymentRequest
        DeploymentRequest updatedDeploymentRequest = deploymentRequestRepository.findById(deploymentRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeploymentRequest are not directly saved in db
        em.detach(updatedDeploymentRequest);
        updatedDeploymentRequest
            .deployId(UPDATED_DEPLOY_ID)
            .deployGroupId(UPDATED_DEPLOY_GROUP_ID)
            .deploySequence(UPDATED_DEPLOY_SEQUENCE)
            .appId(UPDATED_APP_ID)
            .deployName(UPDATED_DEPLOY_NAME)
            .requestComponent(UPDATED_REQUEST_COMPONENT)
            .deployStatus(UPDATED_DEPLOY_STATUS)
            .requestType(UPDATED_REQUEST_TYPE)
            .releaseType(UPDATED_RELEASE_TYPE)
            .moduleName(UPDATED_MODULE_NAME)
            .portal(UPDATED_PORTAL)
            .requestorID(UPDATED_REQUESTOR_ID)
            .jiraId(UPDATED_JIRA_ID)
            .ptsId(UPDATED_PTS_ID)
            .buildId(UPDATED_BUILD_ID)
            .rlmRequestId(UPDATED_RLM_REQUEST_ID)
            .rlmRequestEnv(UPDATED_RLM_REQUEST_ENV)
            .deploymentCategory(UPDATED_DEPLOYMENT_CATEGORY)
            .promotionEnv(UPDATED_PROMOTION_ENV)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .deployCompletedDate(UPDATED_DEPLOY_COMPLETED_DATE)
            .triggerNextSeqDeploy(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY)
            .qaSignOffId(UPDATED_QA_SIGN_OFF_ID)
            .chgno(UPDATED_CHGNO);

        restDeploymentRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeploymentRequest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeploymentRequest))
            )
            .andExpect(status().isOk());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
        DeploymentRequest testDeploymentRequest = deploymentRequestList.get(deploymentRequestList.size() - 1);
        assertThat(testDeploymentRequest.getDeployId()).isEqualTo(UPDATED_DEPLOY_ID);
        assertThat(testDeploymentRequest.getDeployGroupId()).isEqualTo(UPDATED_DEPLOY_GROUP_ID);
        assertThat(testDeploymentRequest.getDeploySequence()).isEqualTo(UPDATED_DEPLOY_SEQUENCE);
        assertThat(testDeploymentRequest.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testDeploymentRequest.getDeployName()).isEqualTo(UPDATED_DEPLOY_NAME);
        assertThat(testDeploymentRequest.getRequestComponent()).isEqualTo(UPDATED_REQUEST_COMPONENT);
        assertThat(testDeploymentRequest.getDeployStatus()).isEqualTo(UPDATED_DEPLOY_STATUS);
        assertThat(testDeploymentRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testDeploymentRequest.getReleaseType()).isEqualTo(UPDATED_RELEASE_TYPE);
        assertThat(testDeploymentRequest.getModuleName()).isEqualTo(UPDATED_MODULE_NAME);
        assertThat(testDeploymentRequest.getPortal()).isEqualTo(UPDATED_PORTAL);
        assertThat(testDeploymentRequest.getRequestorID()).isEqualTo(UPDATED_REQUESTOR_ID);
        assertThat(testDeploymentRequest.getJiraId()).isEqualTo(UPDATED_JIRA_ID);
        assertThat(testDeploymentRequest.getPtsId()).isEqualTo(UPDATED_PTS_ID);
        assertThat(testDeploymentRequest.getBuildId()).isEqualTo(UPDATED_BUILD_ID);
        assertThat(testDeploymentRequest.getRlmRequestId()).isEqualTo(UPDATED_RLM_REQUEST_ID);
        assertThat(testDeploymentRequest.getRlmRequestEnv()).isEqualTo(UPDATED_RLM_REQUEST_ENV);
        assertThat(testDeploymentRequest.getDeploymentCategory()).isEqualTo(UPDATED_DEPLOYMENT_CATEGORY);
        assertThat(testDeploymentRequest.getPromotionEnv()).isEqualTo(UPDATED_PROMOTION_ENV);
        assertThat(testDeploymentRequest.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testDeploymentRequest.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testDeploymentRequest.getDeployCompletedDate()).isEqualTo(UPDATED_DEPLOY_COMPLETED_DATE);
        assertThat(testDeploymentRequest.getTriggerNextSeqDeploy()).isEqualTo(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY);
        assertThat(testDeploymentRequest.getQaSignOffId()).isEqualTo(UPDATED_QA_SIGN_OFF_ID);
        assertThat(testDeploymentRequest.getChgno()).isEqualTo(UPDATED_CHGNO);
    }

    @Test
    @Transactional
    void putNonExistingDeploymentRequest() throws Exception {
        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();
        deploymentRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeploymentRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deploymentRequest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeploymentRequest() throws Exception {
        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();
        deploymentRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeploymentRequest() throws Exception {
        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();
        deploymentRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeploymentRequestWithPatch() throws Exception {
        // Initialize the database
        deploymentRequestRepository.saveAndFlush(deploymentRequest);

        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();

        // Update the deploymentRequest using partial update
        DeploymentRequest partialUpdatedDeploymentRequest = new DeploymentRequest();
        partialUpdatedDeploymentRequest.setId(deploymentRequest.getId());

        partialUpdatedDeploymentRequest
            .deployId(UPDATED_DEPLOY_ID)
            .deploySequence(UPDATED_DEPLOY_SEQUENCE)
            .appId(UPDATED_APP_ID)
            .deployName(UPDATED_DEPLOY_NAME)
            .requestComponent(UPDATED_REQUEST_COMPONENT)
            .deployStatus(UPDATED_DEPLOY_STATUS)
            .requestType(UPDATED_REQUEST_TYPE)
            .releaseType(UPDATED_RELEASE_TYPE)
            .portal(UPDATED_PORTAL)
            .rlmRequestId(UPDATED_RLM_REQUEST_ID)
            .promotionEnv(UPDATED_PROMOTION_ENV)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .triggerNextSeqDeploy(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY);

        restDeploymentRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeploymentRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeploymentRequest))
            )
            .andExpect(status().isOk());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
        DeploymentRequest testDeploymentRequest = deploymentRequestList.get(deploymentRequestList.size() - 1);
        assertThat(testDeploymentRequest.getDeployId()).isEqualTo(UPDATED_DEPLOY_ID);
        assertThat(testDeploymentRequest.getDeployGroupId()).isEqualTo(DEFAULT_DEPLOY_GROUP_ID);
        assertThat(testDeploymentRequest.getDeploySequence()).isEqualTo(UPDATED_DEPLOY_SEQUENCE);
        assertThat(testDeploymentRequest.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testDeploymentRequest.getDeployName()).isEqualTo(UPDATED_DEPLOY_NAME);
        assertThat(testDeploymentRequest.getRequestComponent()).isEqualTo(UPDATED_REQUEST_COMPONENT);
        assertThat(testDeploymentRequest.getDeployStatus()).isEqualTo(UPDATED_DEPLOY_STATUS);
        assertThat(testDeploymentRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testDeploymentRequest.getReleaseType()).isEqualTo(UPDATED_RELEASE_TYPE);
        assertThat(testDeploymentRequest.getModuleName()).isEqualTo(DEFAULT_MODULE_NAME);
        assertThat(testDeploymentRequest.getPortal()).isEqualTo(UPDATED_PORTAL);
        assertThat(testDeploymentRequest.getRequestorID()).isEqualTo(DEFAULT_REQUESTOR_ID);
        assertThat(testDeploymentRequest.getJiraId()).isEqualTo(DEFAULT_JIRA_ID);
        assertThat(testDeploymentRequest.getPtsId()).isEqualTo(DEFAULT_PTS_ID);
        assertThat(testDeploymentRequest.getBuildId()).isEqualTo(DEFAULT_BUILD_ID);
        assertThat(testDeploymentRequest.getRlmRequestId()).isEqualTo(UPDATED_RLM_REQUEST_ID);
        assertThat(testDeploymentRequest.getRlmRequestEnv()).isEqualTo(DEFAULT_RLM_REQUEST_ENV);
        assertThat(testDeploymentRequest.getDeploymentCategory()).isEqualTo(DEFAULT_DEPLOYMENT_CATEGORY);
        assertThat(testDeploymentRequest.getPromotionEnv()).isEqualTo(UPDATED_PROMOTION_ENV);
        assertThat(testDeploymentRequest.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testDeploymentRequest.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testDeploymentRequest.getDeployCompletedDate()).isEqualTo(DEFAULT_DEPLOY_COMPLETED_DATE);
        assertThat(testDeploymentRequest.getTriggerNextSeqDeploy()).isEqualTo(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY);
        assertThat(testDeploymentRequest.getQaSignOffId()).isEqualTo(DEFAULT_QA_SIGN_OFF_ID);
        assertThat(testDeploymentRequest.getChgno()).isEqualTo(DEFAULT_CHGNO);
    }

    @Test
    @Transactional
    void fullUpdateDeploymentRequestWithPatch() throws Exception {
        // Initialize the database
        deploymentRequestRepository.saveAndFlush(deploymentRequest);

        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();

        // Update the deploymentRequest using partial update
        DeploymentRequest partialUpdatedDeploymentRequest = new DeploymentRequest();
        partialUpdatedDeploymentRequest.setId(deploymentRequest.getId());

        partialUpdatedDeploymentRequest
            .deployId(UPDATED_DEPLOY_ID)
            .deployGroupId(UPDATED_DEPLOY_GROUP_ID)
            .deploySequence(UPDATED_DEPLOY_SEQUENCE)
            .appId(UPDATED_APP_ID)
            .deployName(UPDATED_DEPLOY_NAME)
            .requestComponent(UPDATED_REQUEST_COMPONENT)
            .deployStatus(UPDATED_DEPLOY_STATUS)
            .requestType(UPDATED_REQUEST_TYPE)
            .releaseType(UPDATED_RELEASE_TYPE)
            .moduleName(UPDATED_MODULE_NAME)
            .portal(UPDATED_PORTAL)
            .requestorID(UPDATED_REQUESTOR_ID)
            .jiraId(UPDATED_JIRA_ID)
            .ptsId(UPDATED_PTS_ID)
            .buildId(UPDATED_BUILD_ID)
            .rlmRequestId(UPDATED_RLM_REQUEST_ID)
            .rlmRequestEnv(UPDATED_RLM_REQUEST_ENV)
            .deploymentCategory(UPDATED_DEPLOYMENT_CATEGORY)
            .promotionEnv(UPDATED_PROMOTION_ENV)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .deployCompletedDate(UPDATED_DEPLOY_COMPLETED_DATE)
            .triggerNextSeqDeploy(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY)
            .qaSignOffId(UPDATED_QA_SIGN_OFF_ID)
            .chgno(UPDATED_CHGNO);

        restDeploymentRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeploymentRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeploymentRequest))
            )
            .andExpect(status().isOk());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
        DeploymentRequest testDeploymentRequest = deploymentRequestList.get(deploymentRequestList.size() - 1);
        assertThat(testDeploymentRequest.getDeployId()).isEqualTo(UPDATED_DEPLOY_ID);
        assertThat(testDeploymentRequest.getDeployGroupId()).isEqualTo(UPDATED_DEPLOY_GROUP_ID);
        assertThat(testDeploymentRequest.getDeploySequence()).isEqualTo(UPDATED_DEPLOY_SEQUENCE);
        assertThat(testDeploymentRequest.getAppId()).isEqualTo(UPDATED_APP_ID);
        assertThat(testDeploymentRequest.getDeployName()).isEqualTo(UPDATED_DEPLOY_NAME);
        assertThat(testDeploymentRequest.getRequestComponent()).isEqualTo(UPDATED_REQUEST_COMPONENT);
        assertThat(testDeploymentRequest.getDeployStatus()).isEqualTo(UPDATED_DEPLOY_STATUS);
        assertThat(testDeploymentRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testDeploymentRequest.getReleaseType()).isEqualTo(UPDATED_RELEASE_TYPE);
        assertThat(testDeploymentRequest.getModuleName()).isEqualTo(UPDATED_MODULE_NAME);
        assertThat(testDeploymentRequest.getPortal()).isEqualTo(UPDATED_PORTAL);
        assertThat(testDeploymentRequest.getRequestorID()).isEqualTo(UPDATED_REQUESTOR_ID);
        assertThat(testDeploymentRequest.getJiraId()).isEqualTo(UPDATED_JIRA_ID);
        assertThat(testDeploymentRequest.getPtsId()).isEqualTo(UPDATED_PTS_ID);
        assertThat(testDeploymentRequest.getBuildId()).isEqualTo(UPDATED_BUILD_ID);
        assertThat(testDeploymentRequest.getRlmRequestId()).isEqualTo(UPDATED_RLM_REQUEST_ID);
        assertThat(testDeploymentRequest.getRlmRequestEnv()).isEqualTo(UPDATED_RLM_REQUEST_ENV);
        assertThat(testDeploymentRequest.getDeploymentCategory()).isEqualTo(UPDATED_DEPLOYMENT_CATEGORY);
        assertThat(testDeploymentRequest.getPromotionEnv()).isEqualTo(UPDATED_PROMOTION_ENV);
        assertThat(testDeploymentRequest.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testDeploymentRequest.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testDeploymentRequest.getDeployCompletedDate()).isEqualTo(UPDATED_DEPLOY_COMPLETED_DATE);
        assertThat(testDeploymentRequest.getTriggerNextSeqDeploy()).isEqualTo(UPDATED_TRIGGER_NEXT_SEQ_DEPLOY);
        assertThat(testDeploymentRequest.getQaSignOffId()).isEqualTo(UPDATED_QA_SIGN_OFF_ID);
        assertThat(testDeploymentRequest.getChgno()).isEqualTo(UPDATED_CHGNO);
    }

    @Test
    @Transactional
    void patchNonExistingDeploymentRequest() throws Exception {
        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();
        deploymentRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeploymentRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deploymentRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeploymentRequest() throws Exception {
        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();
        deploymentRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeploymentRequest() throws Exception {
        int databaseSizeBeforeUpdate = deploymentRequestRepository.findAll().size();
        deploymentRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeploymentRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deploymentRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeploymentRequest in the database
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeploymentRequest() throws Exception {
        // Initialize the database
        deploymentRequestRepository.saveAndFlush(deploymentRequest);

        int databaseSizeBeforeDelete = deploymentRequestRepository.findAll().size();

        // Delete the deploymentRequest
        restDeploymentRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, deploymentRequest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeploymentRequest> deploymentRequestList = deploymentRequestRepository.findAll();
        assertThat(deploymentRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
