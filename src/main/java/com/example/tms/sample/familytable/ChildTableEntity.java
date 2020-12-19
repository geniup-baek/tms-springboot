package com.example.tms.sample.familytable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sample_children")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "non_foreignkey_parent_id", insertable = false, updatable = false)
    private ParentTableEntity nonForeignkeyParent;
    @Column(name = "non_foreignkey_parent_id")
    private Long nonForeignkeyParentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "foreignkey_parent_id")
    private ParentTableEntity foreignkeyParent;

    @Column(name = "child_field", length = 50)
    private String childField;

    @Version
    private Integer version;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;    

    public void setForCreate() {
        this.id = null;
        this.version = null;
        this.deleted = false;
    }
}
