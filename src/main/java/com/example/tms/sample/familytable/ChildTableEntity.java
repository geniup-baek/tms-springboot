package com.example.tms.sample.familytable;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tms.base.entity.ManagedEntity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sample_children")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class ChildTableEntity extends ManagedEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long childId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "non_foreignkey_parent_id", insertable = false, updatable = false,
            foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ParentTableEntity nonForeignkeyParent;
    @Column(name = "non_foreignkey_parent_id")
    private Long nonForeignkeyParentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "foreignkey_parent_id")
    @Setter
    private ParentTableEntity foreignkeyParent;

    @Column(name = "child_field", length = 50)
    private String childField;

    @Override
    protected void clearId() {
        this.childId = null;
    }

    @Override
    public Long getId() {
        return childId;
    }
}
