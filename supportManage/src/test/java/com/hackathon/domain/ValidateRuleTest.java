package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidateRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidateRule.class);
        ValidateRule validateRule1 = new ValidateRule();
        validateRule1.setId(1L);
        ValidateRule validateRule2 = new ValidateRule();
        validateRule2.setId(validateRule1.getId());
        assertThat(validateRule1).isEqualTo(validateRule2);
        validateRule2.setId(2L);
        assertThat(validateRule1).isNotEqualTo(validateRule2);
        validateRule1.setId(null);
        assertThat(validateRule1).isNotEqualTo(validateRule2);
    }
}
