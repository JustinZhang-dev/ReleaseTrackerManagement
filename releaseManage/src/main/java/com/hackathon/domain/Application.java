package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "app_id")
    private Long appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "app_type")
    private String appType;

    @Column(name = "app_manager")
    private String appManager;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "team_email")
    private String teamEmail;

    @Column(name = "team_location")
    private String teamLocation;

    @Column(name = "production_support_dl")
    private String productionSupportDl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deploymentRequests", "appId" }, allowSetters = true)
    private Set<DeployGroup> deployGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return this.appId;
    }

    public Application appId(Long appId) {
        this.setAppId(appId);
        return this;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return this.appName;
    }

    public Application appName(String appName) {
        this.setAppName(appName);
        return this;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public Application acronym(String acronym) {
        this.setAcronym(acronym);
        return this;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAppType() {
        return this.appType;
    }

    public Application appType(String appType) {
        this.setAppType(appType);
        return this;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppManager() {
        return this.appManager;
    }

    public Application appManager(String appManager) {
        this.setAppManager(appManager);
        return this;
    }

    public void setAppManager(String appManager) {
        this.appManager = appManager;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Application orgName(String orgName) {
        this.setOrgName(orgName);
        return this;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTeamEmail() {
        return this.teamEmail;
    }

    public Application teamEmail(String teamEmail) {
        this.setTeamEmail(teamEmail);
        return this;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }

    public String getTeamLocation() {
        return this.teamLocation;
    }

    public Application teamLocation(String teamLocation) {
        this.setTeamLocation(teamLocation);
        return this;
    }

    public void setTeamLocation(String teamLocation) {
        this.teamLocation = teamLocation;
    }

    public String getProductionSupportDl() {
        return this.productionSupportDl;
    }

    public Application productionSupportDl(String productionSupportDl) {
        this.setProductionSupportDl(productionSupportDl);
        return this;
    }

    public void setProductionSupportDl(String productionSupportDl) {
        this.productionSupportDl = productionSupportDl;
    }

    public Set<DeployGroup> getDeployGroups() {
        return this.deployGroups;
    }

    public void setDeployGroups(Set<DeployGroup> deployGroups) {
        if (this.deployGroups != null) {
            this.deployGroups.forEach(i -> i.setAppId(null));
        }
        if (deployGroups != null) {
            deployGroups.forEach(i -> i.setAppId(this));
        }
        this.deployGroups = deployGroups;
    }

    public Application deployGroups(Set<DeployGroup> deployGroups) {
        this.setDeployGroups(deployGroups);
        return this;
    }

    public Application addDeployGroup(DeployGroup deployGroup) {
        this.deployGroups.add(deployGroup);
        deployGroup.setAppId(this);
        return this;
    }

    public Application removeDeployGroup(DeployGroup deployGroup) {
        this.deployGroups.remove(deployGroup);
        deployGroup.setAppId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", appId=" + getAppId() +
            ", appName='" + getAppName() + "'" +
            ", acronym='" + getAcronym() + "'" +
            ", appType='" + getAppType() + "'" +
            ", appManager='" + getAppManager() + "'" +
            ", orgName='" + getOrgName() + "'" +
            ", teamEmail='" + getTeamEmail() + "'" +
            ", teamLocation='" + getTeamLocation() + "'" +
            ", productionSupportDl='" + getProductionSupportDl() + "'" +
            "}";
    }
}
