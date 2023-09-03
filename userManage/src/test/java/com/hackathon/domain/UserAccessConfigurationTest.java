package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAccessConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAccessConfiguration.class);
        UserAccessConfiguration userAccessConfiguration1 = new UserAccessConfiguration();
        userAccessConfiguration1.setId(1L);
        UserAccessConfiguration userAccessConfiguration2 = new UserAccessConfiguration();
        userAccessConfiguration2.setId(userAccessConfiguration1.getId());
        assertThat(userAccessConfiguration1).isEqualTo(userAccessConfiguration2);
        userAccessConfiguration2.setId(2L);
        assertThat(userAccessConfiguration1).isNotEqualTo(userAccessConfiguration2);
        userAccessConfiguration1.setId(null);
        assertThat(userAccessConfiguration1).isNotEqualTo(userAccessConfiguration2);
    }
}
