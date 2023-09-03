package com.hackathon.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rule.
 */
@Entity
@Table(name = "jhi_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "rule_content")
    private String ruleContent;

    @Column(name = "rule_group")
    private String ruleGroup;

    @Column(name = "rule_seq")
    private Integer ruleSeq;

    @Column(name = "rule_state")
    private String ruleState;

    @Column(name = "rule_map")
    private String ruleMap;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return this.ruleId;
    }

    public Rule ruleId(Long ruleId) {
        this.setRuleId(ruleId);
        return this;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleContent() {
        return this.ruleContent;
    }

    public Rule ruleContent(String ruleContent) {
        this.setRuleContent(ruleContent);
        return this;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public String getRuleGroup() {
        return this.ruleGroup;
    }

    public Rule ruleGroup(String ruleGroup) {
        this.setRuleGroup(ruleGroup);
        return this;
    }

    public void setRuleGroup(String ruleGroup) {
        this.ruleGroup = ruleGroup;
    }

    public Integer getRuleSeq() {
        return this.ruleSeq;
    }

    public Rule ruleSeq(Integer ruleSeq) {
        this.setRuleSeq(ruleSeq);
        return this;
    }

    public void setRuleSeq(Integer ruleSeq) {
        this.ruleSeq = ruleSeq;
    }

    public String getRuleState() {
        return this.ruleState;
    }

    public Rule ruleState(String ruleState) {
        this.setRuleState(ruleState);
        return this;
    }

    public void setRuleState(String ruleState) {
        this.ruleState = ruleState;
    }

    public String getRuleMap() {
        return this.ruleMap;
    }

    public Rule ruleMap(String ruleMap) {
        this.setRuleMap(ruleMap);
        return this;
    }

    public void setRuleMap(String ruleMap) {
        this.ruleMap = ruleMap;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rule)) {
            return false;
        }
        return id != null && id.equals(((Rule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rule{" +
            "id=" + getId() +
            ", ruleId=" + getRuleId() +
            ", ruleContent='" + getRuleContent() + "'" +
            ", ruleGroup='" + getRuleGroup() + "'" +
            ", ruleSeq=" + getRuleSeq() +
            ", ruleState='" + getRuleState() + "'" +
            ", ruleMap='" + getRuleMap() + "'" +
            "}";
    }
}
