package com.example.tms.sample.singletable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.tms.base.entity.ManagedEntity;
import com.example.tms.utility.converter.annotation.ConvertSourceField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sample_singles")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SingleTableEntity extends ManagedEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long singleId;

    @Column(name = "required_string_field", nullable = false, length = 50)
    @ConvertSourceField(name = "requiredString") // DTO field name
    private String requiredStringField;

    @Column(name = "code_field", length = 50)
    @ConvertSourceField(name = "code") // DTO field name
    private String codeField;

    @Override
    protected void clearId() {
        this.singleId = null;
    }

    @Override
    public Long getId() {
        return singleId;
    }

}
