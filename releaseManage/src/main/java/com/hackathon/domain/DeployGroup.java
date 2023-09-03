package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeployGroup.
 */
@Entity
@Table(name = "deploy_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeployGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "deploy_group_id")
    private Long deployGroupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "app_id")
    private Long appId;

    @Column(name = "deploy_manager_id")
    private String deployManagerId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deployGroupId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deployAudits", "qaSignOffId", "deployGroupId" }, allowSetters = true)
    private Set<DeploymentRequest> deploymentRequests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "deployGroups" }, allowSetters = true)
    private Application appId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeployGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeployGroupId() {
        return this.deployGroupId;
    }

    public DeployGroup deployGroupId(Long deployGroupId) {
        this.setDeployGroupId(deployGroupId);
        return this;
    }

    public void setDeployGroupId(Long deployGroupId) {
        this.deployGroupId = deployGroupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public DeployGroup groupName(String groupName) {
        this.setGroupName(groupName);
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getAppId() {
        return this.appId;
    }

    public DeployGroup appId(Long appId) {
        this.setAppId(appId);
        return this;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getDeployManagerId() {
        return this.deployManagerId;
    }

    public DeployGroup deployManagerId(String deployManagerId) {
        this.setDeployManagerId(deployManagerId);
        return this;
    }

    public void setDeployManagerId(String deployManagerId) {
        this.deployManagerId = deployManagerId;
    }

    public Set<DeploymentRequest> getDeploymentRequests() {
        return this.deploymentRequests;
    }

    public void setDeploymentRequests(Set<DeploymentRequest> deploymentRequests) {
        if (this.deploymentRequests != null) {
            this.deploymentRequests.forEach(i -> i.setDeployGroupId(null));
        }
        if (deploymentRequests != null) {
            deploymentRequests.forEach(i -> i.setDeployGroupId(this));
        }
        this.deploymentRequests = deploymentRequests;
    }

    public DeployGroup deploymentRequests(Set<DeploymentRequest> deploymentRequests) {
        this.setDeploymentRequests(deploymentRequests);
        return this;
    }

    public DeployGroup addDeploymentRequest(DeploymentRequest deploymentRequest) {
        this.deploymentRequests.add(deploymentRequest);
        deploymentRequest.setDeployGroupId(this);
        return this;
    }

    public DeployGroup removeDeploymentRequest(DeploymentRequest deploymentRequest) {
        this.deploymentRequests.remove(deploymentRequest);
        deploymentRequest.setDeployGroupId(null);
        return this;
    }

    public Application getAppId() {
        return this.appId;
    }

    public void setAppId(Application application) {
        this.appId = application;
    }

    public DeployGroup appId(Application application) {
        this.setAppId(application);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeployGroup)) {
            return false;
        }
        return id != null && id.equals(((DeployGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeployGroup{" +
            "id=" + getId() +
            ", deployGroupId=" + getDeployGroupId() +
            ", groupName='" + getGroupName() + "'" +
            ", appId=" + getAppId() +
            ", deployManagerId='" + getDeployManagerId() + "'" +
            "}";
    }
}
