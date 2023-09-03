package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.Validation;
import com.hackathon.repository.ValidationRepository;
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
 * Integration tests for the {@link ValidationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValidationResourceIT {

    private static final Long DEFAULT_VALIDATE_ID = 1L;
    private static final Long UPDATED_VALIDATE_ID = 2L;

    private static final Long DEFAULT_VALIDATE_RULE_ID = 1L;
    private static final Long UPDATED_VALIDATE_RULE_ID = 2L;

    private static final String DEFAULT_VALIDATE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATE_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDATE_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATE_RESULT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VALIDATE_FLAG = false;
    private static final Boolean UPDATED_VALIDATE_FLAG = true;

    private static final String ENTITY_API_URL = "/api/validations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValidationMockMvc;

    private Validation validation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Validation createEntity(EntityManager em) {
        Validation validation = new Validation()
            .validateId(DEFAULT_VALIDATE_ID)
            .validateRuleId(DEFAULT_VALIDATE_RULE_ID)
            .validateContent(DEFAULT_VALIDATE_CONTENT)
            .validateResult(DEFAULT_VALIDATE_RESULT)
            .validateFlag(DEFAULT_VALIDATE_FLAG);
        return validation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Validation createUpdatedEntity(EntityManager em) {
        Validation validation = new Validation()
            .validateId(UPDATED_VALIDATE_ID)
            .validateRuleId(UPDATED_VALIDATE_RULE_ID)
            .validateContent(UPDATED_VALIDATE_CONTENT)
            .validateResult(UPDATED_VALIDATE_RESULT)
            .validateFlag(UPDATED_VALIDATE_FLAG);
        return validation;
    }

    @BeforeEach
    public void initTest() {
        validation = createEntity(em);
    }

    @Test
    @Transactional
    void createValidation() throws Exception {
        int databaseSizeBeforeCreate = validationRepository.findAll().size();
        // Create the Validation
        restValidationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isCreated());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeCreate + 1);
        Validation testValidation = validationList.get(validationList.size() - 1);
        assertThat(testValidation.getValidateId()).isEqualTo(DEFAULT_VALIDATE_ID);
        assertThat(testValidation.getValidateRuleId()).isEqualTo(DEFAULT_VALIDATE_RULE_ID);
        assertThat(testValidation.getValidateContent()).isEqualTo(DEFAULT_VALIDATE_CONTENT);
        assertThat(testValidation.getValidateResult()).isEqualTo(DEFAULT_VALIDATE_RESULT);
        assertThat(testValidation.getValidateFlag()).isEqualTo(DEFAULT_VALIDATE_FLAG);
    }

    @Test
    @Transactional
    void createValidationWithExistingId() throws Exception {
        // Create the Validation with an existing ID
        validation.setId(1L);

        int databaseSizeBeforeCreate = validationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllValidations() throws Exception {
        // Initialize the database
        validationRepository.saveAndFlush(validation);

        // Get all the validationList
        restValidationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validation.getId().intValue())))
            .andExpect(jsonPath("$.[*].validateId").value(hasItem(DEFAULT_VALIDATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].validateRuleId").value(hasItem(DEFAULT_VALIDATE_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].validateContent").value(hasItem(DEFAULT_VALIDATE_CONTENT)))
            .andExpect(jsonPath("$.[*].validateResult").value(hasItem(DEFAULT_VALIDATE_RESULT)))
            .andExpect(jsonPath("$.[*].validateFlag").value(hasItem(DEFAULT_VALIDATE_FLAG.booleanValue())));
    }

    @Test
    @Transactional
    void getValidation() throws Exception {
        // Initialize the database
        validationRepository.saveAndFlush(validation);

        // Get the validation
        restValidationMockMvc
            .perform(get(ENTITY_API_URL_ID, validation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(validation.getId().intValue()))
            .andExpect(jsonPath("$.validateId").value(DEFAULT_VALIDATE_ID.intValue()))
            .andExpect(jsonPath("$.validateRuleId").value(DEFAULT_VALIDATE_RULE_ID.intValue()))
            .andExpect(jsonPath("$.validateContent").value(DEFAULT_VALIDATE_CONTENT))
            .andExpect(jsonPath("$.validateResult").value(DEFAULT_VALIDATE_RESULT))
            .andExpect(jsonPath("$.validateFlag").value(DEFAULT_VALIDATE_FLAG.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingValidation() throws Exception {
        // Get the validation
        restValidationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingValidation() throws Exception {
        // Initialize the database
        validationRepository.saveAndFlush(validation);

        int databaseSizeBeforeUpdate = validationRepository.findAll().size();

        // Update the validation
        Validation updatedValidation = validationRepository.findById(validation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedValidation are not directly saved in db
        em.detach(updatedValidation);
        updatedValidation
            .validateId(UPDATED_VALIDATE_ID)
            .validateRuleId(UPDATED_VALIDATE_RULE_ID)
            .validateContent(UPDATED_VALIDATE_CONTENT)
            .validateResult(UPDATED_VALIDATE_RESULT)
            .validateFlag(UPDATED_VALIDATE_FLAG);

        restValidationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedValidation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedValidation))
            )
            .andExpect(status().isOk());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
        Validation testValidation = validationList.get(validationList.size() - 1);
        assertThat(testValidation.getValidateId()).isEqualTo(UPDATED_VALIDATE_ID);
        assertThat(testValidation.getValidateRuleId()).isEqualTo(UPDATED_VALIDATE_RULE_ID);
        assertThat(testValidation.getValidateContent()).isEqualTo(UPDATED_VALIDATE_CONTENT);
        assertThat(testValidation.getValidateResult()).isEqualTo(UPDATED_VALIDATE_RESULT);
        assertThat(testValidation.getValidateFlag()).isEqualTo(UPDATED_VALIDATE_FLAG);
    }

    @Test
    @Transactional
    void putNonExistingValidation() throws Exception {
        int databaseSizeBeforeUpdate = validationRepository.findAll().size();
        validation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValidation() throws Exception {
        int databaseSizeBeforeUpdate = validationRepository.findAll().size();
        validation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValidation() throws Exception {
        int databaseSizeBeforeUpdate = validationRepository.findAll().size();
        validation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValidationWithPatch() throws Exception {
        // Initialize the database
        validationRepository.saveAndFlush(validation);

        int databaseSizeBeforeUpdate = validationRepository.findAll().size();

        // Update the validation using partial update
        Validation partialUpdatedValidation = new Validation();
        partialUpdatedValidation.setId(validation.getId());

        partialUpdatedValidation
            .validateRuleId(UPDATED_VALIDATE_RULE_ID)
            .validateContent(UPDATED_VALIDATE_CONTENT)
            .validateFlag(UPDATED_VALIDATE_FLAG);

        restValidationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidation))
            )
            .andExpect(status().isOk());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
        Validation testValidation = validationList.get(validationList.size() - 1);
        assertThat(testValidation.getValidateId()).isEqualTo(DEFAULT_VALIDATE_ID);
        assertThat(testValidation.getValidateRuleId()).isEqualTo(UPDATED_VALIDATE_RULE_ID);
        assertThat(testValidation.getValidateContent()).isEqualTo(UPDATED_VALIDATE_CONTENT);
        assertThat(testValidation.getValidateResult()).isEqualTo(DEFAULT_VALIDATE_RESULT);
        assertThat(testValidation.getValidateFlag()).isEqualTo(UPDATED_VALIDATE_FLAG);
    }

    @Test
    @Transactional
    void fullUpdateValidationWithPatch() throws Exception {
        // Initialize the database
        validationRepository.saveAndFlush(validation);

        int databaseSizeBeforeUpdate = validationRepository.findAll().size();

        // Update the validation using partial update
        Validation partialUpdatedValidation = new Validation();
        partialUpdatedValidation.setId(validation.getId());

        partialUpdatedValidation
            .validateId(UPDATED_VALIDATE_ID)
            .validateRuleId(UPDATED_VALIDATE_RULE_ID)
            .validateContent(UPDATED_VALIDATE_CONTENT)
            .validateResult(UPDATED_VALIDATE_RESULT)
            .validateFlag(UPDATED_VALIDATE_FLAG);

        restValidationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidation))
            )
            .andExpect(status().isOk());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
        Validation testValidation = validationList.get(validationList.size() - 1);
        assertThat(testValidation.getValidateId()).isEqualTo(UPDATED_VALIDATE_ID);
        assertThat(testValidation.getValidateRuleId()).isEqualTo(UPDATED_VALIDATE_RULE_ID);
        assertThat(testValidation.getValidateContent()).isEqualTo(UPDATED_VALIDATE_CONTENT);
        assertThat(testValidation.getValidateResult()).isEqualTo(UPDATED_VALIDATE_RESULT);
        assertThat(testValidation.getValidateFlag()).isEqualTo(UPDATED_VALIDATE_FLAG);
    }

    @Test
    @Transactional
    void patchNonExistingValidation() throws Exception {
        int databaseSizeBeforeUpdate = validationRepository.findAll().size();
        validation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, validation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValidation() throws Exception {
        int databaseSizeBeforeUpdate = validationRepository.findAll().size();
        validation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValidation() throws Exception {
        int databaseSizeBeforeUpdate = validationRepository.findAll().size();
        validation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Validation in the database
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValidation() throws Exception {
        // Initialize the database
        validationRepository.saveAndFlush(validation);

        int databaseSizeBeforeDelete = validationRepository.findAll().size();

        // Delete the validation
        restValidationMockMvc
            .perform(delete(ENTITY_API_URL_ID, validation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Validation> validationList = validationRepository.findAll();
        assertThat(validationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
