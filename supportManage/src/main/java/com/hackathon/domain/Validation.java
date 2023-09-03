package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Validation.
 */
@Entity
@Table(name = "jhi_validation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Validation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "validate_id")
    private Long validateId;

    @Column(name = "validate_rule_id")
    private Long validateRuleId;

    @Column(name = "validate_content")
    private String validateContent;

    @Column(name = "validate_result")
    private String validateResult;

    @Column(name = "validate_flag")
    private Boolean validateFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "validations" }, allowSetters = true)
    private ValidateRule validateRuleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Validation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValidateId() {
        return this.validateId;
    }

    public Validation validateId(Long validateId) {
        this.setValidateId(validateId);
        return this;
    }

    public void setValidateId(Long validateId) {
        this.validateId = validateId;
    }

    public Long getValidateRuleId() {
        return this.validateRuleId;
    }

    public Validation validateRuleId(Long validateRuleId) {
        this.setValidateRuleId(validateRuleId);
        return this;
    }

    public void setValidateRuleId(Long validateRuleId) {
        this.validateRuleId = validateRuleId;
    }

    public String getValidateContent() {
        return this.validateContent;
    }

    public Validation validateContent(String validateContent) {
        this.setValidateContent(validateContent);
        return this;
    }

    public void setValidateContent(String validateContent) {
        this.validateContent = validateContent;
    }

    public String getValidateResult() {
        return this.validateResult;
    }

    public Validation validateResult(String validateResult) {
        this.setValidateResult(validateResult);
        return this;
    }

    public void setValidateResult(String validateResult) {
        this.validateResult = validateResult;
    }

    public Boolean getValidateFlag() {
        return this.validateFlag;
    }

    public Validation validateFlag(Boolean validateFlag) {
        this.setValidateFlag(validateFlag);
        return this;
    }

    public void setValidateFlag(Boolean validateFlag) {
        this.validateFlag = validateFlag;
    }

    public ValidateRule getValidateRuleId() {
        return this.validateRuleId;
    }

    public void setValidateRuleId(ValidateRule validateRule) {
        this.validateRuleId = validateRule;
    }

    public Validation validateRuleId(ValidateRule validateRule) {
        this.setValidateRuleId(validateRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Validation)) {
            return false;
        }
        return id != null && id.equals(((Validation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Validation{" +
            "id=" + getId() +
            ", validateId=" + getValidateId() +
            ", validateRuleId=" + getValidateRuleId() +
            ", validateContent='" + getValidateContent() + "'" +
            ", validateResult='" + getValidateResult() + "'" +
            ", validateFlag='" + getValidateFlag() + "'" +
            "}";
    }
}
