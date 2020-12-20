package com.example.tms.sample.manytomany.userblockrole;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tms.base.entity.ManagedEntity;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sample_role_quotas")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class SampleRoleQuota extends ManagedEntity<SampleRoleQuotaPk> implements Serializable {
    
	private static final long serialVersionUID = -6713561733680723406L;

	@EmbeddedId
    private SampleRoleQuotaPk pk;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name", nullable = false)
    @Setter
    private SampleRole role;
    
    @Override
    protected void clearId() {
        this.pk = null;
    }

    @Override
    public SampleRoleQuotaPk getId() {
        return pk;
    }
}
