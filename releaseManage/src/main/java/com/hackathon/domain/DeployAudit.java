package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeployAudit.
 */
@Entity
@Table(name = "deploy_audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeployAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "deploy_id")
    private Long deployId;

    @Column(name = "deploy_former_status")
    private String deployFormerStatus;

    @Column(name = "deployed_status")
    private String deployedStatus;

    @Column(name = "deployed_operation_type")
    private String deployedOperationType;

    @Column(name = "action_performed_role")
    private String actionPerformedRole;

    @Column(name = "action_performed_by")
    private String actionPerformedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "deployAudits", "qaSignOffId", "deployGroupId" }, allowSetters = true)
    private DeploymentRequest deployId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeployAudit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuditId() {
        return this.auditId;
    }

    public DeployAudit auditId(Long auditId) {
        this.setAuditId(auditId);
        return this;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getDeployId() {
        return this.deployId;
    }

    public DeployAudit deployId(Long deployId) {
        this.setDeployId(deployId);
        return this;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public String getDeployFormerStatus() {
        return this.deployFormerStatus;
    }

    public DeployAudit deployFormerStatus(String deployFormerStatus) {
        this.setDeployFormerStatus(deployFormerStatus);
        return this;
    }

    public void setDeployFormerStatus(String deployFormerStatus) {
        this.deployFormerStatus = deployFormerStatus;
    }

    public String getDeployedStatus() {
        return this.deployedStatus;
    }

    public DeployAudit deployedStatus(String deployedStatus) {
        this.setDeployedStatus(deployedStatus);
        return this;
    }

    public void setDeployedStatus(String deployedStatus) {
        this.deployedStatus = deployedStatus;
    }

    public String getDeployedOperationType() {
        return this.deployedOperationType;
    }

    public DeployAudit deployedOperationType(String deployedOperationType) {
        this.setDeployedOperationType(deployedOperationType);
        return this;
    }

    public void setDeployedOperationType(String deployedOperationType) {
        this.deployedOperationType = deployedOperationType;
    }

    public String getActionPerformedRole() {
        return this.actionPerformedRole;
    }

    public DeployAudit actionPerformedRole(String actionPerformedRole) {
        this.setActionPerformedRole(actionPerformedRole);
        return this;
    }

    public void setActionPerformedRole(String actionPerformedRole) {
        this.actionPerformedRole = actionPerformedRole;
    }

    public String getActionPerformedBy() {
        return this.actionPerformedBy;
    }

    public DeployAudit actionPerformedBy(String actionPerformedBy) {
        this.setActionPerformedBy(actionPerformedBy);
        return this;
    }

    public void setActionPerformedBy(String actionPerformedBy) {
        this.actionPerformedBy = actionPerformedBy;
    }

    public DeploymentRequest getDeployId() {
        return this.deployId;
    }

    public void setDeployId(DeploymentRequest deploymentRequest) {
        this.deployId = deploymentRequest;
    }

    public DeployAudit deployId(DeploymentRequest deploymentRequest) {
        this.setDeployId(deploymentRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeployAudit)) {
            return false;
        }
        return id != null && id.equals(((DeployAudit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeployAudit{" +
            "id=" + getId() +
            ", auditId=" + getAuditId() +
            ", deployId=" + getDeployId() +
            ", deployFormerStatus='" + getDeployFormerStatus() + "'" +
            ", deployedStatus='" + getDeployedStatus() + "'" +
            ", deployedOperationType='" + getDeployedOperationType() + "'" +
            ", actionPerformedRole='" + getActionPerformedRole() + "'" +
            ", actionPerformedBy='" + getActionPerformedBy() + "'" +
            "}";
    }
}
