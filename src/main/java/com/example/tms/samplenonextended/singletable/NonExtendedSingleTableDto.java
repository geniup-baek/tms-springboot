package com.example.tms.samplenonextended.singletable;

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
public class NonExtendedSingleTableDto {
    private Long singleId;
    private String requiredString;
    private String code;
    private String name;
    private Integer version;
    private Boolean deleted;
}
