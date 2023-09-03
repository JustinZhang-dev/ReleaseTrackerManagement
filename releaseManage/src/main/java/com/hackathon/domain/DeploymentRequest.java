package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeploymentRequest.
 */
@Entity
@Table(name = "deployment_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeploymentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "deploy_id")
    private Long deployId;

    @Column(name = "deploy_group_id")
    private Long deployGroupId;

    @Column(name = "deploy_sequence")
    private Integer deploySequence;

    @Column(name = "app_id")
    private Long appId;

    @Column(name = "deploy_name")
    private String deployName;

    @Column(name = "request_component")
    private String requestComponent;

    @Column(name = "deploy_status")
    private String deployStatus;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "release_type")
    private String releaseType;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "portal")
    private String portal;

    @Column(name = "requestor_id")
    private String requestorID;

    @Column(name = "jira_id")
    private String jiraId;

    @Column(name = "pts_id")
    private Long ptsId;

    @Column(name = "build_id")
    private String buildId;

    @Column(name = "rlm_request_id")
    private String rlmRequestId;

    @Column(name = "rlm_request_env")
    private String rlmRequestEnv;

    @Column(name = "deployment_category")
    private String deploymentCategory;

    @Column(name = "promotion_env")
    private String promotionEnv;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "deploy_completed_date")
    private Instant deployCompletedDate;

    @Column(name = "trigger_next_seq_deploy")
    private Boolean triggerNextSeqDeploy;

    @Column(name = "qa_sign_off_id")
    private Long qaSignOffId;

    @Column(name = "chgno")
    private String chgno;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deployId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deployId" }, allowSetters = true)
    private Set<DeployAudit> deployAudits = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private QASignOff qaSignOffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "deploymentRequests", "appId" }, allowSetters = true)
    private DeployGroup deployGroupId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeploymentRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeployId() {
        return this.deployId;
    }

    public DeploymentRequest deployId(Long deployId) {
        this.setDeployId(deployId);
        return this;
    }

    public void setDeployId(Long deployId) {
        this.deployId = deployId;
    }

    public Long getDeployGroupId() {
        return this.deployGroupId;
    }

    public DeploymentRequest deployGroupId(Long deployGroupId) {
        this.setDeployGroupId(deployGroupId);
        return this;
    }

    public void setDeployGroupId(Long deployGroupId) {
        this.deployGroupId = deployGroupId;
    }

    public Integer getDeploySequence() {
        return this.deploySequence;
    }

    public DeploymentRequest deploySequence(Integer deploySequence) {
        this.setDeploySequence(deploySequence);
        return this;
    }

    public void setDeploySequence(Integer deploySequence) {
        this.deploySequence = deploySequence;
    }

    public Long getAppId() {
        return this.appId;
    }

    public DeploymentRequest appId(Long appId) {
        this.setAppId(appId);
        return this;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getDeployName() {
        return this.deployName;
    }

    public DeploymentRequest deployName(String deployName) {
        this.setDeployName(deployName);
        return this;
    }

    public void setDeployName(String deployName) {
        this.deployName = deployName;
    }

    public String getRequestComponent() {
        return this.requestComponent;
    }

    public DeploymentRequest requestComponent(String requestComponent) {
        this.setRequestComponent(requestComponent);
        return this;
    }

    public void setRequestComponent(String requestComponent) {
        this.requestComponent = requestComponent;
    }

    public String getDeployStatus() {
        return this.deployStatus;
    }

    public DeploymentRequest deployStatus(String deployStatus) {
        this.setDeployStatus(deployStatus);
        return this;
    }

    public void setDeployStatus(String deployStatus) {
        this.deployStatus = deployStatus;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public DeploymentRequest requestType(String requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getReleaseType() {
        return this.releaseType;
    }

    public DeploymentRequest releaseType(String releaseType) {
        this.setReleaseType(releaseType);
        return this;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public DeploymentRequest moduleName(String moduleName) {
        this.setModuleName(moduleName);
        return this;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPortal() {
        return this.portal;
    }

    public DeploymentRequest portal(String portal) {
        this.setPortal(portal);
        return this;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getRequestorID() {
        return this.requestorID;
    }

    public DeploymentRequest requestorID(String requestorID) {
        this.setRequestorID(requestorID);
        return this;
    }

    public void setRequestorID(String requestorID) {
        this.requestorID = requestorID;
    }

    public String getJiraId() {
        return this.jiraId;
    }

    public DeploymentRequest jiraId(String jiraId) {
        this.setJiraId(jiraId);
        return this;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public Long getPtsId() {
        return this.ptsId;
    }

    public DeploymentRequest ptsId(Long ptsId) {
        this.setPtsId(ptsId);
        return this;
    }

    public void setPtsId(Long ptsId) {
        this.ptsId = ptsId;
    }

    public String getBuildId() {
        return this.buildId;
    }

    public DeploymentRequest buildId(String buildId) {
        this.setBuildId(buildId);
        return this;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getRlmRequestId() {
        return this.rlmRequestId;
    }

    public DeploymentRequest rlmRequestId(String rlmRequestId) {
        this.setRlmRequestId(rlmRequestId);
        return this;
    }

    public void setRlmRequestId(String rlmRequestId) {
        this.rlmRequestId = rlmRequestId;
    }

    public String getRlmRequestEnv() {
        return this.rlmRequestEnv;
    }

    public DeploymentRequest rlmRequestEnv(String rlmRequestEnv) {
        this.setRlmRequestEnv(rlmRequestEnv);
        return this;
    }

    public void setRlmRequestEnv(String rlmRequestEnv) {
        this.rlmRequestEnv = rlmRequestEnv;
    }

    public String getDeploymentCategory() {
        return this.deploymentCategory;
    }

    public DeploymentRequest deploymentCategory(String deploymentCategory) {
        this.setDeploymentCategory(deploymentCategory);
        return this;
    }

    public void setDeploymentCategory(String deploymentCategory) {
        this.deploymentCategory = deploymentCategory;
    }

    public String getPromotionEnv() {
        return this.promotionEnv;
    }

    public DeploymentRequest promotionEnv(String promotionEnv) {
        this.setPromotionEnv(promotionEnv);
        return this;
    }

    public void setPromotionEnv(String promotionEnv) {
        this.promotionEnv = promotionEnv;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public DeploymentRequest startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public DeploymentRequest endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getDeployCompletedDate() {
        return this.deployCompletedDate;
    }

    public DeploymentRequest deployCompletedDate(Instant deployCompletedDate) {
        this.setDeployCompletedDate(deployCompletedDate);
        return this;
    }

    public void setDeployCompletedDate(Instant deployCompletedDate) {
        this.deployCompletedDate = deployCompletedDate;
    }

    public Boolean getTriggerNextSeqDeploy() {
        return this.triggerNextSeqDeploy;
    }

    public DeploymentRequest triggerNextSeqDeploy(Boolean triggerNextSeqDeploy) {
        this.setTriggerNextSeqDeploy(triggerNextSeqDeploy);
        return this;
    }

    public void setTriggerNextSeqDeploy(Boolean triggerNextSeqDeploy) {
        this.triggerNextSeqDeploy = triggerNextSeqDeploy;
    }

    public Long getQaSignOffId() {
        return this.qaSignOffId;
    }

    public DeploymentRequest qaSignOffId(Long qaSignOffId) {
        this.setQaSignOffId(qaSignOffId);
        return this;
    }

    public void setQaSignOffId(Long qaSignOffId) {
        this.qaSignOffId = qaSignOffId;
    }

    public String getChgno() {
        return this.chgno;
    }

    public DeploymentRequest chgno(String chgno) {
        this.setChgno(chgno);
        return this;
    }

    public void setChgno(String chgno) {
        this.chgno = chgno;
    }

    public Set<DeployAudit> getDeployAudits() {
        return this.deployAudits;
    }

    public void setDeployAudits(Set<DeployAudit> deployAudits) {
        if (this.deployAudits != null) {
            this.deployAudits.forEach(i -> i.setDeployId(null));
        }
        if (deployAudits != null) {
            deployAudits.forEach(i -> i.setDeployId(this));
        }
        this.deployAudits = deployAudits;
    }

    public DeploymentRequest deployAudits(Set<DeployAudit> deployAudits) {
        this.setDeployAudits(deployAudits);
        return this;
    }

    public DeploymentRequest addDeployAudit(DeployAudit deployAudit) {
        this.deployAudits.add(deployAudit);
        deployAudit.setDeployId(this);
        return this;
    }

    public DeploymentRequest removeDeployAudit(DeployAudit deployAudit) {
        this.deployAudits.remove(deployAudit);
        deployAudit.setDeployId(null);
        return this;
    }

    public QASignOff getQaSignOffId() {
        return this.qaSignOffId;
    }

    public void setQaSignOffId(QASignOff qASignOff) {
        this.qaSignOffId = qASignOff;
    }

    public DeploymentRequest qaSignOffId(QASignOff qASignOff) {
        this.setQaSignOffId(qASignOff);
        return this;
    }

    public DeployGroup getDeployGroupId() {
        return this.deployGroupId;
    }

    public void setDeployGroupId(DeployGroup deployGroup) {
        this.deployGroupId = deployGroup;
    }

    public DeploymentRequest deployGroupId(DeployGroup deployGroup) {
        this.setDeployGroupId(deployGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeploymentRequest)) {
            return false;
        }
        return id != null && id.equals(((DeploymentRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeploymentRequest{" +
            "id=" + getId() +
            ", deployId=" + getDeployId() +
            ", deployGroupId=" + getDeployGroupId() +
            ", deploySequence=" + getDeploySequence() +
            ", appId=" + getAppId() +
            ", deployName='" + getDeployName() + "'" +
            ", requestComponent='" + getRequestComponent() + "'" +
            ", deployStatus='" + getDeployStatus() + "'" +
            ", requestType='" + getRequestType() + "'" +
            ", releaseType='" + getReleaseType() + "'" +
            ", moduleName='" + getModuleName() + "'" +
            ", portal='" + getPortal() + "'" +
            ", requestorID='" + getRequestorID() + "'" +
            ", jiraId='" + getJiraId() + "'" +
            ", ptsId=" + getPtsId() +
            ", buildId='" + getBuildId() + "'" +
            ", rlmRequestId='" + getRlmRequestId() + "'" +
            ", rlmRequestEnv='" + getRlmRequestEnv() + "'" +
            ", deploymentCategory='" + getDeploymentCategory() + "'" +
            ", promotionEnv='" + getPromotionEnv() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", deployCompletedDate='" + getDeployCompletedDate() + "'" +
            ", triggerNextSeqDeploy='" + getTriggerNextSeqDeploy() + "'" +
            ", qaSignOffId=" + getQaSignOffId() +
            ", chgno='" + getChgno() + "'" +
            "}";
    }
}
