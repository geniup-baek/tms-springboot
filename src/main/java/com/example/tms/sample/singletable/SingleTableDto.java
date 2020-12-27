package com.example.tms.sample.singletable;

import com.example.tms.base.dto.CrudDto;
import com.example.tms.utility.converter.annotation.ConvertSourceField;

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
public class SingleTableDto implements CrudDto<Long> {

    private Long singleId;

    @ConvertSourceField(name = "requiredStringField") // Entity field name
    private String requiredString;

    @ConvertSourceField(name = "codeField") // Entity field name
    private String code;

    private String name;

    private Integer version;
    
    private Boolean deleted;

    @Override
    public Long getId() {
        return singleId;
    }
}
