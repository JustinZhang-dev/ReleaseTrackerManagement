package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeploymentRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeploymentRequest.class);
        DeploymentRequest deploymentRequest1 = new DeploymentRequest();
        deploymentRequest1.setId(1L);
        DeploymentRequest deploymentRequest2 = new DeploymentRequest();
        deploymentRequest2.setId(deploymentRequest1.getId());
        assertThat(deploymentRequest1).isEqualTo(deploymentRequest2);
        deploymentRequest2.setId(2L);
        assertThat(deploymentRequest1).isNotEqualTo(deploymentRequest2);
        deploymentRequest1.setId(null);
        assertThat(deploymentRequest1).isNotEqualTo(deploymentRequest2);
    }
}
