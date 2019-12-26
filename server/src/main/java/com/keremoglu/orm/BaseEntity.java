package com.keremoglu.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdTimestamp", "updatedTimestamp"},
        allowGetters = true
)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private Date createdTimestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_user_id", nullable = true)
    @JsonIgnore
    private User createdUser;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "updated_timestamp", nullable = true)
    private Date updatedTimestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_user_id", nullable = true)
    private User updatedUser;

    @Version
    @Basic
    @Column(name = "version")
    private int version;

    @PrePersist
    public void prePersist() {
        this.createdTimestamp = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedTimestamp = new Date();
    }

    @PreRemove
    public void preRemove() { /*Removal logic */}

    public abstract Map toMap();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public User getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(User updatedUser) {
        this.updatedUser = updatedUser;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
