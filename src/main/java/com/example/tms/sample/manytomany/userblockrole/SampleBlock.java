package com.example.tms.sample.manytomany.userblockrole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.tms.base.entity.ManagedEntity;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sample_blocks")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class SampleBlock extends ManagedEntity<String> implements Serializable {

    private static final long serialVersionUID = -1337552361062864119L;

    @Id
    @Column(name = "block_code", length = 50)
    private String blockCode;

    @Column(name = "block_name", length = 256)
    private String blockName;

    @OneToMany(mappedBy = "pk.block")
    @Builder.Default
    private List<SampleRoleQuota> users = new ArrayList<>();
    
    @Override
    protected void clearId() {
        this.blockCode = null;
    }

    @Override
    public String getId() {
        return blockCode;
    }
}
