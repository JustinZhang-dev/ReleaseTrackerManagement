package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeployGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeployGroup.class);
        DeployGroup deployGroup1 = new DeployGroup();
        deployGroup1.setId(1L);
        DeployGroup deployGroup2 = new DeployGroup();
        deployGroup2.setId(deployGroup1.getId());
        assertThat(deployGroup1).isEqualTo(deployGroup2);
        deployGroup2.setId(2L);
        assertThat(deployGroup1).isNotEqualTo(deployGroup2);
        deployGroup1.setId(null);
        assertThat(deployGroup1).isNotEqualTo(deployGroup2);
    }
}
