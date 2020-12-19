package com.example.tms.samplenonextended.familytable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sample_nonextended_parents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class NonExtendedParentTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long parentId;

    @Column(name = "parent_field", length = 50)
    private String parentField;

    @Version
    private Integer version;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;    

    public void setForCreate() {
        this.parentId = null;
        this.version = null;
        this.deleted = false;
    }
}
