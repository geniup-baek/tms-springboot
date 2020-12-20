package com.example.tms.sample.manytomany.userblockrole;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode(of = {"user", "block"})
public class SampleRoleQuotaPk implements Serializable {

    private static final long serialVersionUID = 8703553521347852436L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SampleUser user; 

    @ManyToOne
    @JoinColumn(name = "block_code")
    private SampleBlock block;
}
