package com.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hackathon.domain.enumeration.UserType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_soe_id")
    private String userSoeId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userId" }, allowSetters = true)
    private Set<UserAccessConfiguration> userAccessConfigurations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserInfo userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserSoeId() {
        return this.userSoeId;
    }

    public UserInfo userSoeId(String userSoeId) {
        this.setUserSoeId(userSoeId);
        return this;
    }

    public void setUserSoeId(String userSoeId) {
        this.userSoeId = userSoeId;
    }

    public String getUserName() {
        return this.userName;
    }

    public UserInfo userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public UserInfo userEmail(String userEmail) {
        this.setUserEmail(userEmail);
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public UserInfo userType(UserType userType) {
        this.setUserType(userType);
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<UserAccessConfiguration> getUserAccessConfigurations() {
        return this.userAccessConfigurations;
    }

    public void setUserAccessConfigurations(Set<UserAccessConfiguration> userAccessConfigurations) {
        if (this.userAccessConfigurations != null) {
            this.userAccessConfigurations.forEach(i -> i.setUserId(null));
        }
        if (userAccessConfigurations != null) {
            userAccessConfigurations.forEach(i -> i.setUserId(this));
        }
        this.userAccessConfigurations = userAccessConfigurations;
    }

    public UserInfo userAccessConfigurations(Set<UserAccessConfiguration> userAccessConfigurations) {
        this.setUserAccessConfigurations(userAccessConfigurations);
        return this;
    }

    public UserInfo addUserAccessConfiguration(UserAccessConfiguration userAccessConfiguration) {
        this.userAccessConfigurations.add(userAccessConfiguration);
        userAccessConfiguration.setUserId(this);
        return this;
    }

    public UserInfo removeUserAccessConfiguration(UserAccessConfiguration userAccessConfiguration) {
        this.userAccessConfigurations.remove(userAccessConfiguration);
        userAccessConfiguration.setUserId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfo)) {
            return false;
        }
        return id != null && id.equals(((UserInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", userSoeId='" + getUserSoeId() + "'" +
            ", userName='" + getUserName() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", userType='" + getUserType() + "'" +
            "}";
    }
}
