package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ValidateRule.
 */
@Entity
@Table(name = "validate_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ValidateRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "validate_rule_id")
    private Long validateRuleId;

    @Column(name = "validate_rule")
    private String validateRule;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "validateRuleId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "validateRuleId" }, allowSetters = true)
    private Set<Validation> validations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ValidateRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValidateRuleId() {
        return this.validateRuleId;
    }

    public ValidateRule validateRuleId(Long validateRuleId) {
        this.setValidateRuleId(validateRuleId);
        return this;
    }

    public void setValidateRuleId(Long validateRuleId) {
        this.validateRuleId = validateRuleId;
    }

    public String getValidateRule() {
        return this.validateRule;
    }

    public ValidateRule validateRule(String validateRule) {
        this.setValidateRule(validateRule);
        return this;
    }

    public void setValidateRule(String validateRule) {
        this.validateRule = validateRule;
    }

    public Set<Validation> getValidations() {
        return this.validations;
    }

    public void setValidations(Set<Validation> validations) {
        if (this.validations != null) {
            this.validations.forEach(i -> i.setValidateRuleId(null));
        }
        if (validations != null) {
            validations.forEach(i -> i.setValidateRuleId(this));
        }
        this.validations = validations;
    }

    public ValidateRule validations(Set<Validation> validations) {
        this.setValidations(validations);
        return this;
    }

    public ValidateRule addValidation(Validation validation) {
        this.validations.add(validation);
        validation.setValidateRuleId(this);
        return this;
    }

    public ValidateRule removeValidation(Validation validation) {
        this.validations.remove(validation);
        validation.setValidateRuleId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidateRule)) {
            return false;
        }
        return id != null && id.equals(((ValidateRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidateRule{" +
            "id=" + getId() +
            ", validateRuleId=" + getValidateRuleId() +
            ", validateRule='" + getValidateRule() + "'" +
            "}";
    }
}
