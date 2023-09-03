package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.UserInfo;
import com.hackathon.domain.enumeration.UserType;
import com.hackathon.repository.UserInfoRepository;
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
 * Integration tests for the {@link UserInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserInfoResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_USER_SOE_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_SOE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final UserType DEFAULT_USER_TYPE = UserType.DEV;
    private static final UserType UPDATED_USER_TYPE = UserType.DEVOPS;

    private static final String ENTITY_API_URL = "/api/user-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .userId(DEFAULT_USER_ID)
            .userSoeId(DEFAULT_USER_SOE_ID)
            .userName(DEFAULT_USER_NAME)
            .userEmail(DEFAULT_USER_EMAIL)
            .userType(DEFAULT_USER_TYPE);
        return userInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createUpdatedEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .userId(UPDATED_USER_ID)
            .userSoeId(UPDATED_USER_SOE_ID)
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userType(UPDATED_USER_TYPE);
        return userInfo;
    }

    @BeforeEach
    public void initTest() {
        userInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();
        // Create the UserInfo
        restUserInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserInfo.getUserSoeId()).isEqualTo(DEFAULT_USER_SOE_ID);
        assertThat(testUserInfo.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUserInfo.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testUserInfo.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
    }

    @Test
    @Transactional
    void createUserInfoWithExistingId() throws Exception {
        // Create the UserInfo with an existing ID
        userInfo.setId(1L);

        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList
        restUserInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userSoeId").value(hasItem(DEFAULT_USER_SOE_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())));
    }

    @Test
    @Transactional
    void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.userSoeId").value(DEFAULT_USER_SOE_ID))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserInfo are not directly saved in db
        em.detach(updatedUserInfo);
        updatedUserInfo
            .userId(UPDATED_USER_ID)
            .userSoeId(UPDATED_USER_SOE_ID)
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userType(UPDATED_USER_TYPE);

        restUserInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserInfo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserInfo))
            )
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserInfo.getUserSoeId()).isEqualTo(UPDATED_USER_SOE_ID);
        assertThat(testUserInfo.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUserInfo.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testUserInfo.getUserType()).isEqualTo(UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();
        userInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userInfo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();
        userInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();
        userInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserInfoWithPatch() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo using partial update
        UserInfo partialUpdatedUserInfo = new UserInfo();
        partialUpdatedUserInfo.setId(userInfo.getId());

        partialUpdatedUserInfo.userSoeId(UPDATED_USER_SOE_ID);

        restUserInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserInfo))
            )
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserInfo.getUserSoeId()).isEqualTo(UPDATED_USER_SOE_ID);
        assertThat(testUserInfo.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUserInfo.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testUserInfo.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateUserInfoWithPatch() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo using partial update
        UserInfo partialUpdatedUserInfo = new UserInfo();
        partialUpdatedUserInfo.setId(userInfo.getId());

        partialUpdatedUserInfo
            .userId(UPDATED_USER_ID)
            .userSoeId(UPDATED_USER_SOE_ID)
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userType(UPDATED_USER_TYPE);

        restUserInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserInfo))
            )
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserInfo.getUserSoeId()).isEqualTo(UPDATED_USER_SOE_ID);
        assertThat(testUserInfo.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUserInfo.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testUserInfo.getUserType()).isEqualTo(UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();
        userInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();
        userInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();
        userInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Delete the userInfo
        restUserInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, userInfo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
