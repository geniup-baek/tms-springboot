package com.example.tms.sample.familytable;

import com.example.tms.base.BaseSearchCondition;

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
public class FamilySearchCondition implements BaseSearchCondition<Long> {

    private Long parentId;
    private String parent;

    @Override
    public Long getId() {
        return this.parentId;
    }

    @Override
    public void setId(Long id) {
        this.parentId = id;
    }    
}
