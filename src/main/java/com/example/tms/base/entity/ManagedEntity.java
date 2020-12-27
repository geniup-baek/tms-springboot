package com.example.tms.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.example.tms.base.BaseEntity;
import com.example.tms.utility.converter.annotation.MergeIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public abstract class ManagedEntity<ID> implements BaseEntity {
    
    @Version
    @MergeIgnore
    private Integer version;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;

    public void setForCreate() {
        clearId();
        this.version = null;
        this.deleted = false;
    }

    protected abstract void clearId();

    public abstract ID getId();
}
