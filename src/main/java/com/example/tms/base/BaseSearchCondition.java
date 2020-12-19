package com.example.tms.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface BaseSearchCondition<ID> {

    @JsonIgnore
    ID getId();
    void setId(ID id);
}
