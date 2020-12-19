package com.example.tms.sample.code;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode(of = {"codeKind", "code"})
public class CodePk implements Serializable {
    private static final long serialVersionUID = -1263838005753552627L;

    @Column(name = "code_kind", length = 128)
    private String codeKind;

    @Column(name = "code", length = 128)
    private String code;
}
