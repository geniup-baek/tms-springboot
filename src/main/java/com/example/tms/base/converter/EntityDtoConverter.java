package com.example.tms.base.converter;

import com.example.tms.base.BaseConverter;
import com.example.tms.base.BaseEntity;
import com.example.tms.base.dto.CrudDto;

public interface EntityDtoConverter<E extends BaseEntity, D extends CrudDto<ID>, ID> extends BaseConverter {

    E fromDto(D dto);
    E fromDtoForCreate(D dto);
    D fromEntity(E entity);
    void mergeEntity(E target, E source);
}
