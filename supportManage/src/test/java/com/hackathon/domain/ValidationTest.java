package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Validation.class);
        Validation validation1 = new Validation();
        validation1.setId(1L);
        Validation validation2 = new Validation();
        validation2.setId(validation1.getId());
        assertThat(validation1).isEqualTo(validation2);
        validation2.setId(2L);
        assertThat(validation1).isNotEqualTo(validation2);
        validation1.setId(null);
        assertThat(validation1).isNotEqualTo(validation2);
    }
}
