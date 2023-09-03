package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.QASignOff;
import com.hackathon.repository.QASignOffRepository;
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
 * Integration tests for the {@link QASignOffResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QASignOffResourceIT {

    private static final Long DEFAULT_SIGN_OFF_ID = 1L;
    private static final Long UPDATED_SIGN_OFF_ID = 2L;

    private static final Boolean DEFAULT_QA_TESTED = false;
    private static final Boolean UPDATED_QA_TESTED = true;

    private static final String DEFAULT_QA_TESTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_QA_TESTED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SIGN_OFF_ATTACHED = false;
    private static final Boolean UPDATED_SIGN_OFF_ATTACHED = true;

    private static final String DEFAULT_SIGN_OFF_TO = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_OFF_TO = "BBBBBBBBBB";

    private static final String DEFAULT_SIGN_OFF_CC = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_OFF_CC = "BBBBBBBBBB";

    private static final String DEFAULT_SIGN_OFF_ATTACHMENTS = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_OFF_ATTACHMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/qa-sign-offs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QASignOffRepository qASignOffRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQASignOffMockMvc;

    private QASignOff qASignOff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QASignOff createEntity(EntityManager em) {
        QASignOff qASignOff = new QASignOff()
            .signOffId(DEFAULT_SIGN_OFF_ID)
            .qaTested(DEFAULT_QA_TESTED)
            .qaTestedBy(DEFAULT_QA_TESTED_BY)
            .signOffAttached(DEFAULT_SIGN_OFF_ATTACHED)
            .signOffTo(DEFAULT_SIGN_OFF_TO)
            .signOffCc(DEFAULT_SIGN_OFF_CC)
            .signOffAttachments(DEFAULT_SIGN_OFF_ATTACHMENTS);
        return qASignOff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QASignOff createUpdatedEntity(EntityManager em) {
        QASignOff qASignOff = new QASignOff()
            .signOffId(UPDATED_SIGN_OFF_ID)
            .qaTested(UPDATED_QA_TESTED)
            .qaTestedBy(UPDATED_QA_TESTED_BY)
            .signOffAttached(UPDATED_SIGN_OFF_ATTACHED)
            .signOffTo(UPDATED_SIGN_OFF_TO)
            .signOffCc(UPDATED_SIGN_OFF_CC)
            .signOffAttachments(UPDATED_SIGN_OFF_ATTACHMENTS);
        return qASignOff;
    }

    @BeforeEach
    public void initTest() {
        qASignOff = createEntity(em);
    }

    @Test
    @Transactional
    void createQASignOff() throws Exception {
        int databaseSizeBeforeCreate = qASignOffRepository.findAll().size();
        // Create the QASignOff
        restQASignOffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isCreated());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeCreate + 1);
        QASignOff testQASignOff = qASignOffList.get(qASignOffList.size() - 1);
        assertThat(testQASignOff.getSignOffId()).isEqualTo(DEFAULT_SIGN_OFF_ID);
        assertThat(testQASignOff.getQaTested()).isEqualTo(DEFAULT_QA_TESTED);
        assertThat(testQASignOff.getQaTestedBy()).isEqualTo(DEFAULT_QA_TESTED_BY);
        assertThat(testQASignOff.getSignOffAttached()).isEqualTo(DEFAULT_SIGN_OFF_ATTACHED);
        assertThat(testQASignOff.getSignOffTo()).isEqualTo(DEFAULT_SIGN_OFF_TO);
        assertThat(testQASignOff.getSignOffCc()).isEqualTo(DEFAULT_SIGN_OFF_CC);
        assertThat(testQASignOff.getSignOffAttachments()).isEqualTo(DEFAULT_SIGN_OFF_ATTACHMENTS);
    }

    @Test
    @Transactional
    void createQASignOffWithExistingId() throws Exception {
        // Create the QASignOff with an existing ID
        qASignOff.setId(1L);

        int databaseSizeBeforeCreate = qASignOffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQASignOffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQASignOffs() throws Exception {
        // Initialize the database
        qASignOffRepository.saveAndFlush(qASignOff);

        // Get all the qASignOffList
        restQASignOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qASignOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].signOffId").value(hasItem(DEFAULT_SIGN_OFF_ID.intValue())))
            .andExpect(jsonPath("$.[*].qaTested").value(hasItem(DEFAULT_QA_TESTED.booleanValue())))
            .andExpect(jsonPath("$.[*].qaTestedBy").value(hasItem(DEFAULT_QA_TESTED_BY)))
            .andExpect(jsonPath("$.[*].signOffAttached").value(hasItem(DEFAULT_SIGN_OFF_ATTACHED.booleanValue())))
            .andExpect(jsonPath("$.[*].signOffTo").value(hasItem(DEFAULT_SIGN_OFF_TO)))
            .andExpect(jsonPath("$.[*].signOffCc").value(hasItem(DEFAULT_SIGN_OFF_CC)))
            .andExpect(jsonPath("$.[*].signOffAttachments").value(hasItem(DEFAULT_SIGN_OFF_ATTACHMENTS)));
    }

    @Test
    @Transactional
    void getQASignOff() throws Exception {
        // Initialize the database
        qASignOffRepository.saveAndFlush(qASignOff);

        // Get the qASignOff
        restQASignOffMockMvc
            .perform(get(ENTITY_API_URL_ID, qASignOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qASignOff.getId().intValue()))
            .andExpect(jsonPath("$.signOffId").value(DEFAULT_SIGN_OFF_ID.intValue()))
            .andExpect(jsonPath("$.qaTested").value(DEFAULT_QA_TESTED.booleanValue()))
            .andExpect(jsonPath("$.qaTestedBy").value(DEFAULT_QA_TESTED_BY))
            .andExpect(jsonPath("$.signOffAttached").value(DEFAULT_SIGN_OFF_ATTACHED.booleanValue()))
            .andExpect(jsonPath("$.signOffTo").value(DEFAULT_SIGN_OFF_TO))
            .andExpect(jsonPath("$.signOffCc").value(DEFAULT_SIGN_OFF_CC))
            .andExpect(jsonPath("$.signOffAttachments").value(DEFAULT_SIGN_OFF_ATTACHMENTS));
    }

    @Test
    @Transactional
    void getNonExistingQASignOff() throws Exception {
        // Get the qASignOff
        restQASignOffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQASignOff() throws Exception {
        // Initialize the database
        qASignOffRepository.saveAndFlush(qASignOff);

        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();

        // Update the qASignOff
        QASignOff updatedQASignOff = qASignOffRepository.findById(qASignOff.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQASignOff are not directly saved in db
        em.detach(updatedQASignOff);
        updatedQASignOff
            .signOffId(UPDATED_SIGN_OFF_ID)
            .qaTested(UPDATED_QA_TESTED)
            .qaTestedBy(UPDATED_QA_TESTED_BY)
            .signOffAttached(UPDATED_SIGN_OFF_ATTACHED)
            .signOffTo(UPDATED_SIGN_OFF_TO)
            .signOffCc(UPDATED_SIGN_OFF_CC)
            .signOffAttachments(UPDATED_SIGN_OFF_ATTACHMENTS);

        restQASignOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQASignOff.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQASignOff))
            )
            .andExpect(status().isOk());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
        QASignOff testQASignOff = qASignOffList.get(qASignOffList.size() - 1);
        assertThat(testQASignOff.getSignOffId()).isEqualTo(UPDATED_SIGN_OFF_ID);
        assertThat(testQASignOff.getQaTested()).isEqualTo(UPDATED_QA_TESTED);
        assertThat(testQASignOff.getQaTestedBy()).isEqualTo(UPDATED_QA_TESTED_BY);
        assertThat(testQASignOff.getSignOffAttached()).isEqualTo(UPDATED_SIGN_OFF_ATTACHED);
        assertThat(testQASignOff.getSignOffTo()).isEqualTo(UPDATED_SIGN_OFF_TO);
        assertThat(testQASignOff.getSignOffCc()).isEqualTo(UPDATED_SIGN_OFF_CC);
        assertThat(testQASignOff.getSignOffAttachments()).isEqualTo(UPDATED_SIGN_OFF_ATTACHMENTS);
    }

    @Test
    @Transactional
    void putNonExistingQASignOff() throws Exception {
        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();
        qASignOff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQASignOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qASignOff.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQASignOff() throws Exception {
        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();
        qASignOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQASignOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQASignOff() throws Exception {
        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();
        qASignOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQASignOffMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQASignOffWithPatch() throws Exception {
        // Initialize the database
        qASignOffRepository.saveAndFlush(qASignOff);

        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();

        // Update the qASignOff using partial update
        QASignOff partialUpdatedQASignOff = new QASignOff();
        partialUpdatedQASignOff.setId(qASignOff.getId());

        partialUpdatedQASignOff.qaTestedBy(UPDATED_QA_TESTED_BY).signOffAttached(UPDATED_SIGN_OFF_ATTACHED).signOffTo(UPDATED_SIGN_OFF_TO);

        restQASignOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQASignOff.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQASignOff))
            )
            .andExpect(status().isOk());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
        QASignOff testQASignOff = qASignOffList.get(qASignOffList.size() - 1);
        assertThat(testQASignOff.getSignOffId()).isEqualTo(DEFAULT_SIGN_OFF_ID);
        assertThat(testQASignOff.getQaTested()).isEqualTo(DEFAULT_QA_TESTED);
        assertThat(testQASignOff.getQaTestedBy()).isEqualTo(UPDATED_QA_TESTED_BY);
        assertThat(testQASignOff.getSignOffAttached()).isEqualTo(UPDATED_SIGN_OFF_ATTACHED);
        assertThat(testQASignOff.getSignOffTo()).isEqualTo(UPDATED_SIGN_OFF_TO);
        assertThat(testQASignOff.getSignOffCc()).isEqualTo(DEFAULT_SIGN_OFF_CC);
        assertThat(testQASignOff.getSignOffAttachments()).isEqualTo(DEFAULT_SIGN_OFF_ATTACHMENTS);
    }

    @Test
    @Transactional
    void fullUpdateQASignOffWithPatch() throws Exception {
        // Initialize the database
        qASignOffRepository.saveAndFlush(qASignOff);

        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();

        // Update the qASignOff using partial update
        QASignOff partialUpdatedQASignOff = new QASignOff();
        partialUpdatedQASignOff.setId(qASignOff.getId());

        partialUpdatedQASignOff
            .signOffId(UPDATED_SIGN_OFF_ID)
            .qaTested(UPDATED_QA_TESTED)
            .qaTestedBy(UPDATED_QA_TESTED_BY)
            .signOffAttached(UPDATED_SIGN_OFF_ATTACHED)
            .signOffTo(UPDATED_SIGN_OFF_TO)
            .signOffCc(UPDATED_SIGN_OFF_CC)
            .signOffAttachments(UPDATED_SIGN_OFF_ATTACHMENTS);

        restQASignOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQASignOff.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQASignOff))
            )
            .andExpect(status().isOk());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
        QASignOff testQASignOff = qASignOffList.get(qASignOffList.size() - 1);
        assertThat(testQASignOff.getSignOffId()).isEqualTo(UPDATED_SIGN_OFF_ID);
        assertThat(testQASignOff.getQaTested()).isEqualTo(UPDATED_QA_TESTED);
        assertThat(testQASignOff.getQaTestedBy()).isEqualTo(UPDATED_QA_TESTED_BY);
        assertThat(testQASignOff.getSignOffAttached()).isEqualTo(UPDATED_SIGN_OFF_ATTACHED);
        assertThat(testQASignOff.getSignOffTo()).isEqualTo(UPDATED_SIGN_OFF_TO);
        assertThat(testQASignOff.getSignOffCc()).isEqualTo(UPDATED_SIGN_OFF_CC);
        assertThat(testQASignOff.getSignOffAttachments()).isEqualTo(UPDATED_SIGN_OFF_ATTACHMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingQASignOff() throws Exception {
        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();
        qASignOff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQASignOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, qASignOff.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQASignOff() throws Exception {
        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();
        qASignOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQASignOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQASignOff() throws Exception {
        int databaseSizeBeforeUpdate = qASignOffRepository.findAll().size();
        qASignOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQASignOffMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qASignOff))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QASignOff in the database
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQASignOff() throws Exception {
        // Initialize the database
        qASignOffRepository.saveAndFlush(qASignOff);

        int databaseSizeBeforeDelete = qASignOffRepository.findAll().size();

        // Delete the qASignOff
        restQASignOffMockMvc
            .perform(delete(ENTITY_API_URL_ID, qASignOff.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QASignOff> qASignOffList = qASignOffRepository.findAll();
        assertThat(qASignOffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
