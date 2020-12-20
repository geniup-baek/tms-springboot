package com.example.tms.sample.manytomany.userblockrole;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "sample_roles")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class SampleRole extends ManagedEntity<String> implements Serializable{
    private static final long serialVersionUID = 1761495251698526009L;

    @Id
    @Column(name = "role_name", length = 256)
    private String roleName;

    @Override
    protected void clearId() {
        this.roleName = null;
    }

    @Override
    public String getId() {
        return roleName;
    }
}
