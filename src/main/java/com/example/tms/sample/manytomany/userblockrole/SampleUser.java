package com.example.tms.sample.manytomany.userblockrole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.tms.base.entity.ManagedEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sample_users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class SampleUser extends ManagedEntity<String> implements Serializable {
    private static final long serialVersionUID = 7493402686177953448L;

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @OneToMany(mappedBy = "pk.user")
    @Builder.Default
    private List<SampleRoleQuota> blocks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "default_block", referencedColumnName = "block_code", nullable = false)
    @Setter
    private SampleBlock defaultBlock;

    @Column(name = "default_lang", nullable = false, length = 255)
    @ColumnDefault("'ja'")
    private String defaultLang;    

    @Override
    protected void clearId() {
        this.userId = null;
    }

    @Override
    public String getId() {
        return userId;
    }
}
