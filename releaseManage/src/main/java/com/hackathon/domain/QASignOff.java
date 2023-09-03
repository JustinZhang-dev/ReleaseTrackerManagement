package com.hackathon.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QASignOff.
 */
@Entity
@Table(name = "qa_sign_off")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QASignOff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sign_off_id")
    private Long signOffId;

    @Column(name = "qa_tested")
    private Boolean qaTested;

    @Column(name = "qa_tested_by")
    private String qaTestedBy;

    @Column(name = "sign_off_attached")
    private Boolean signOffAttached;

    @Column(name = "sign_off_to")
    private String signOffTo;

    @Column(name = "sign_off_cc")
    private String signOffCc;

    @Column(name = "sign_off_attachments")
    private String signOffAttachments;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QASignOff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSignOffId() {
        return this.signOffId;
    }

    public QASignOff signOffId(Long signOffId) {
        this.setSignOffId(signOffId);
        return this;
    }

    public void setSignOffId(Long signOffId) {
        this.signOffId = signOffId;
    }

    public Boolean getQaTested() {
        return this.qaTested;
    }

    public QASignOff qaTested(Boolean qaTested) {
        this.setQaTested(qaTested);
        return this;
    }

    public void setQaTested(Boolean qaTested) {
        this.qaTested = qaTested;
    }

    public String getQaTestedBy() {
        return this.qaTestedBy;
    }

    public QASignOff qaTestedBy(String qaTestedBy) {
        this.setQaTestedBy(qaTestedBy);
        return this;
    }

    public void setQaTestedBy(String qaTestedBy) {
        this.qaTestedBy = qaTestedBy;
    }

    public Boolean getSignOffAttached() {
        return this.signOffAttached;
    }

    public QASignOff signOffAttached(Boolean signOffAttached) {
        this.setSignOffAttached(signOffAttached);
        return this;
    }

    public void setSignOffAttached(Boolean signOffAttached) {
        this.signOffAttached = signOffAttached;
    }

    public String getSignOffTo() {
        return this.signOffTo;
    }

    public QASignOff signOffTo(String signOffTo) {
        this.setSignOffTo(signOffTo);
        return this;
    }

    public void setSignOffTo(String signOffTo) {
        this.signOffTo = signOffTo;
    }

    public String getSignOffCc() {
        return this.signOffCc;
    }

    public QASignOff signOffCc(String signOffCc) {
        this.setSignOffCc(signOffCc);
        return this;
    }

    public void setSignOffCc(String signOffCc) {
        this.signOffCc = signOffCc;
    }

    public String getSignOffAttachments() {
        return this.signOffAttachments;
    }

    public QASignOff signOffAttachments(String signOffAttachments) {
        this.setSignOffAttachments(signOffAttachments);
        return this;
    }

    public void setSignOffAttachments(String signOffAttachments) {
        this.signOffAttachments = signOffAttachments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QASignOff)) {
            return false;
        }
        return id != null && id.equals(((QASignOff) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QASignOff{" +
            "id=" + getId() +
            ", signOffId=" + getSignOffId() +
            ", qaTested='" + getQaTested() + "'" +
            ", qaTestedBy='" + getQaTestedBy() + "'" +
            ", signOffAttached='" + getSignOffAttached() + "'" +
            ", signOffTo='" + getSignOffTo() + "'" +
            ", signOffCc='" + getSignOffCc() + "'" +
            ", signOffAttachments='" + getSignOffAttachments() + "'" +
            "}";
    }
}
