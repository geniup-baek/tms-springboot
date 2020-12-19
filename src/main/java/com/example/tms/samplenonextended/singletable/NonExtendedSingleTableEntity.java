package com.example.tms.samplenonextended.singletable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sample_nonextended_singles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NonExtendedSingleTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long singleId;

    @Column(name = "required_string_field", nullable = false, length = 50)
    private String requiredStringField;

    @Column(name = "code_field", length = 50)
    private String codeField;

    @Version
    private Integer version;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;    

    public void setForCreate() {
        this.singleId = null;
        this.version = null;
        this.deleted = false;
    }
}
