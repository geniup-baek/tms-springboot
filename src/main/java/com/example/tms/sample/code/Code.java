package com.example.tms.sample.code;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sample_codes")
public class Code {
    
    @EmbeddedId
    private CodePk pk;

    @Column(name = "value", nullable = false, length = 128)
    private String value;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}
