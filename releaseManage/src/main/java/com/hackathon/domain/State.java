package com.hackathon.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A State.
 */
@Entity
@Table(name = "state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class State implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "state")
    private String state;

    @Column(name = "state_seq")
    private Integer stateSeq;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public State id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public State stateId(Long stateId) {
        this.setStateId(stateId);
        return this;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getState() {
        return this.state;
    }

    public State state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getStateSeq() {
        return this.stateSeq;
    }

    public State stateSeq(Integer stateSeq) {
        this.setStateSeq(stateSeq);
        return this;
    }

    public void setStateSeq(Integer stateSeq) {
        this.stateSeq = stateSeq;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        return id != null && id.equals(((State) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "State{" +
            "id=" + getId() +
            ", stateId=" + getStateId() +
            ", state='" + getState() + "'" +
            ", stateSeq=" + getStateSeq() +
            "}";
    }
}
