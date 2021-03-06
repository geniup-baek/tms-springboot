package com.example.tms.sample.familytable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.tms.base.entity.ManagedEntity;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sample_parents")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class ParentTableEntity extends ManagedEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long parentId;

    @Column(name = "parent_field", length = 50)
    private String parentField;

    @Override
    protected void clearId() {
        this.parentId = null;
    }

    @Override
    public Long getId() {
        return parentId;
    }
}
