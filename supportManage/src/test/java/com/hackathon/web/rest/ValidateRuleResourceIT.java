package com.hackathon.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hackathon.IntegrationTest;
import com.hackathon.domain.ValidateRule;
import com.hackathon.repository.ValidateRuleRepository;
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
 * Integration tests for the {@link ValidateRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValidateRuleResourceIT {

    private static final Long DEFAULT_VALIDATE_RULE_ID = 1L;
    private static final Long UPDATED_VALIDATE_RULE_ID = 2L;

    private static final String DEFAULT_VALIDATE_RULE = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATE_RULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/validate-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValidateRuleRepository validateRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValidateRuleMockMvc;

    private ValidateRule validateRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidateRule createEntity(EntityManager em) {
        ValidateRule validateRule = new ValidateRule().validateRuleId(DEFAULT_VALIDATE_RULE_ID).validateRule(DEFAULT_VALIDATE_RULE);
        return validateRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidateRule createUpdatedEntity(EntityManager em) {
        ValidateRule validateRule = new ValidateRule().validateRuleId(UPDATED_VALIDATE_RULE_ID).validateRule(UPDATED_VALIDATE_RULE);
        return validateRule;
    }

    @BeforeEach
    public void initTest() {
        validateRule = createEntity(em);
    }

    @Test
    @Transactional
    void createValidateRule() throws Exception {
        int databaseSizeBeforeCreate = validateRuleRepository.findAll().size();
        // Create the ValidateRule
        restValidateRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isCreated());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeCreate + 1);
        ValidateRule testValidateRule = validateRuleList.get(validateRuleList.size() - 1);
        assertThat(testValidateRule.getValidateRuleId()).isEqualTo(DEFAULT_VALIDATE_RULE_ID);
        assertThat(testValidateRule.getValidateRule()).isEqualTo(DEFAULT_VALIDATE_RULE);
    }

    @Test
    @Transactional
    void createValidateRuleWithExistingId() throws Exception {
        // Create the ValidateRule with an existing ID
        validateRule.setId(1L);

        int databaseSizeBeforeCreate = validateRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidateRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllValidateRules() throws Exception {
        // Initialize the database
        validateRuleRepository.saveAndFlush(validateRule);

        // Get all the validateRuleList
        restValidateRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validateRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].validateRuleId").value(hasItem(DEFAULT_VALIDATE_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].validateRule").value(hasItem(DEFAULT_VALIDATE_RULE)));
    }

    @Test
    @Transactional
    void getValidateRule() throws Exception {
        // Initialize the database
        validateRuleRepository.saveAndFlush(validateRule);

        // Get the validateRule
        restValidateRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, validateRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(validateRule.getId().intValue()))
            .andExpect(jsonPath("$.validateRuleId").value(DEFAULT_VALIDATE_RULE_ID.intValue()))
            .andExpect(jsonPath("$.validateRule").value(DEFAULT_VALIDATE_RULE));
    }

    @Test
    @Transactional
    void getNonExistingValidateRule() throws Exception {
        // Get the validateRule
        restValidateRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingValidateRule() throws Exception {
        // Initialize the database
        validateRuleRepository.saveAndFlush(validateRule);

        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();

        // Update the validateRule
        ValidateRule updatedValidateRule = validateRuleRepository.findById(validateRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedValidateRule are not directly saved in db
        em.detach(updatedValidateRule);
        updatedValidateRule.validateRuleId(UPDATED_VALIDATE_RULE_ID).validateRule(UPDATED_VALIDATE_RULE);

        restValidateRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedValidateRule.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedValidateRule))
            )
            .andExpect(status().isOk());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
        ValidateRule testValidateRule = validateRuleList.get(validateRuleList.size() - 1);
        assertThat(testValidateRule.getValidateRuleId()).isEqualTo(UPDATED_VALIDATE_RULE_ID);
        assertThat(testValidateRule.getValidateRule()).isEqualTo(UPDATED_VALIDATE_RULE);
    }

    @Test
    @Transactional
    void putNonExistingValidateRule() throws Exception {
        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();
        validateRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidateRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validateRule.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValidateRule() throws Exception {
        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();
        validateRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidateRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValidateRule() throws Exception {
        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();
        validateRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidateRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValidateRuleWithPatch() throws Exception {
        // Initialize the database
        validateRuleRepository.saveAndFlush(validateRule);

        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();

        // Update the validateRule using partial update
        ValidateRule partialUpdatedValidateRule = new ValidateRule();
        partialUpdatedValidateRule.setId(validateRule.getId());

        partialUpdatedValidateRule.validateRuleId(UPDATED_VALIDATE_RULE_ID).validateRule(UPDATED_VALIDATE_RULE);

        restValidateRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidateRule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidateRule))
            )
            .andExpect(status().isOk());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
        ValidateRule testValidateRule = validateRuleList.get(validateRuleList.size() - 1);
        assertThat(testValidateRule.getValidateRuleId()).isEqualTo(UPDATED_VALIDATE_RULE_ID);
        assertThat(testValidateRule.getValidateRule()).isEqualTo(UPDATED_VALIDATE_RULE);
    }

    @Test
    @Transactional
    void fullUpdateValidateRuleWithPatch() throws Exception {
        // Initialize the database
        validateRuleRepository.saveAndFlush(validateRule);

        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();

        // Update the validateRule using partial update
        ValidateRule partialUpdatedValidateRule = new ValidateRule();
        partialUpdatedValidateRule.setId(validateRule.getId());

        partialUpdatedValidateRule.validateRuleId(UPDATED_VALIDATE_RULE_ID).validateRule(UPDATED_VALIDATE_RULE);

        restValidateRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidateRule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValidateRule))
            )
            .andExpect(status().isOk());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
        ValidateRule testValidateRule = validateRuleList.get(validateRuleList.size() - 1);
        assertThat(testValidateRule.getValidateRuleId()).isEqualTo(UPDATED_VALIDATE_RULE_ID);
        assertThat(testValidateRule.getValidateRule()).isEqualTo(UPDATED_VALIDATE_RULE);
    }

    @Test
    @Transactional
    void patchNonExistingValidateRule() throws Exception {
        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();
        validateRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidateRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, validateRule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValidateRule() throws Exception {
        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();
        validateRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidateRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValidateRule() throws Exception {
        int databaseSizeBeforeUpdate = validateRuleRepository.findAll().size();
        validateRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidateRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(validateRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidateRule in the database
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValidateRule() throws Exception {
        // Initialize the database
        validateRuleRepository.saveAndFlush(validateRule);

        int databaseSizeBeforeDelete = validateRuleRepository.findAll().size();

        // Delete the validateRule
        restValidateRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, validateRule.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValidateRule> validateRuleList = validateRuleRepository.findAll();
        assertThat(validateRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
