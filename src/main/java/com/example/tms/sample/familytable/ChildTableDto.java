package com.example.tms.sample.familytable;

import com.example.tms.base.dto.CrudDto;

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
public class ChildTableDto implements CrudDto<Long> {
    private Long childId;
    private Long nonForeignkeyParentId;
    private String nonForeignkeyParentParentField;
    private Long foreignkeyParentId;
    private String foreignkeyParentParentField;
    private String child;
    private Integer version;
    private Boolean deleted;

    private String executeType; // C:Create, U:Update, D:Delete

    @Override
    public Long getId() {
        return childId;
    }    
}
