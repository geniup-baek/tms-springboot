package com.example.tms.base.dto;

import com.example.tms.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface CrudDto<ID> extends BaseDto {
    @JsonIgnore
    ID getId();

    Integer getVersion();
}
