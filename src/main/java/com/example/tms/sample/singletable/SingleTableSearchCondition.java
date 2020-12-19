package com.example.tms.sample.singletable;

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
public class SingleTableSearchCondition implements BaseSearchCondition<Long> {
    private Long singleTableId;
    private String requiredString;
    private String code;
    private String name;

    @Override
    public Long getId() {
        return this.singleTableId;
    }

    @Override
    public void setId(Long id) {
        this.singleTableId = id;
    }
}
