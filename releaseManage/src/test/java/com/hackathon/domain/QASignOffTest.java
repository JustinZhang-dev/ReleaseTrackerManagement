package com.hackathon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hackathon.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QASignOffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QASignOff.class);
        QASignOff qASignOff1 = new QASignOff();
        qASignOff1.setId(1L);
        QASignOff qASignOff2 = new QASignOff();
        qASignOff2.setId(qASignOff1.getId());
        assertThat(qASignOff1).isEqualTo(qASignOff2);
        qASignOff2.setId(2L);
        assertThat(qASignOff1).isNotEqualTo(qASignOff2);
        qASignOff1.setId(null);
        assertThat(qASignOff1).isNotEqualTo(qASignOff2);
    }
}
