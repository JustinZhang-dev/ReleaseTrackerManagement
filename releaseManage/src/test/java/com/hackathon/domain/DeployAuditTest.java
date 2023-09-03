package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeployAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeployAudit.class);
        DeployAudit deployAudit1 = new DeployAudit();
        deployAudit1.setId(1L);
        DeployAudit deployAudit2 = new DeployAudit();
        deployAudit2.setId(deployAudit1.getId());
        assertThat(deployAudit1).isEqualTo(deployAudit2);
        deployAudit2.setId(2L);
        assertThat(deployAudit1).isNotEqualTo(deployAudit2);
        deployAudit1.setId(null);
        assertThat(deployAudit1).isNotEqualTo(deployAudit2);
    }
}
