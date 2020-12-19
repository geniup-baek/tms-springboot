package com.example.tms.sample.familytable;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDto {

    private Long id;
    private String parent;
    private Integer version;
    private Boolean deleted;

    @Builder.Default()
    List<ChildTableEntity> children = new ArrayList<>();
}
