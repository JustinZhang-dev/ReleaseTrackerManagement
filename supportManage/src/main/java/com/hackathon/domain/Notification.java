package com.hackathon.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "notify_seq")
    private Long notifySeq;

    @Column(name = "notify_type")
    private String notifyType;

    @Column(name = "notify_to")
    private String notifyTo;

    @Column(name = "notify_by")
    private String notifyBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotificationId() {
        return this.notificationId;
    }

    public Notification notificationId(Long notificationId) {
        this.setNotificationId(notificationId);
        return this;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getNotifySeq() {
        return this.notifySeq;
    }

    public Notification notifySeq(Long notifySeq) {
        this.setNotifySeq(notifySeq);
        return this;
    }

    public void setNotifySeq(Long notifySeq) {
        this.notifySeq = notifySeq;
    }

    public String getNotifyType() {
        return this.notifyType;
    }

    public Notification notifyType(String notifyType) {
        this.setNotifyType(notifyType);
        return this;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyTo() {
        return this.notifyTo;
    }

    public Notification notifyTo(String notifyTo) {
        this.setNotifyTo(notifyTo);
        return this;
    }

    public void setNotifyTo(String notifyTo) {
        this.notifyTo = notifyTo;
    }

    public String getNotifyBy() {
        return this.notifyBy;
    }

    public Notification notifyBy(String notifyBy) {
        this.setNotifyBy(notifyBy);
        return this;
    }

    public void setNotifyBy(String notifyBy) {
        this.notifyBy = notifyBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", notificationId=" + getNotificationId() +
            ", notifySeq=" + getNotifySeq() +
            ", notifyType='" + getNotifyType() + "'" +
            ", notifyTo='" + getNotifyTo() + "'" +
            ", notifyBy='" + getNotifyBy() + "'" +
            "}";
    }
}
