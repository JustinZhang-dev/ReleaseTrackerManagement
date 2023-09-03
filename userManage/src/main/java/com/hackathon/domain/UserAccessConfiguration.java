package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hackathon.domain.enumeration.ActionType;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAccessConfiguration.
 */
@Entity
@Table(name = "user_access_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccessConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_config_id")
    private Long userConfigId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "app_id")
    private Long appId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userAccessConfigurations" }, allowSetters = true)
    private UserInfo userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAccessConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserConfigId() {
        return this.userConfigId;
    }

    public UserAccessConfiguration userConfigId(Long userConfigId) {
        this.setUserConfigId(userConfigId);
        return this;
    }

    public void setUserConfigId(Long userConfigId) {
        this.userConfigId = userConfigId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserAccessConfiguration userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return this.appId;
    }

    public UserAccessConfiguration appId(Long appId) {
        this.setAppId(appId);
        return this;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public UserAccessConfiguration actionType(ActionType actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public UserInfo getUserId() {
        return this.userId;
    }

    public void setUserId(UserInfo userInfo) {
        this.userId = userInfo;
    }

    public UserAccessConfiguration userId(UserInfo userInfo) {
        this.setUserId(userInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccessConfiguration)) {
            return false;
        }
        return id != null && id.equals(((UserAccessConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccessConfiguration{" +
            "id=" + getId() +
            ", userConfigId=" + getUserConfigId() +
            ", userId=" + getUserId() +
            ", appId=" + getAppId() +
            ", actionType='" + getActionType() + "'" +
            "}";
    }
}
