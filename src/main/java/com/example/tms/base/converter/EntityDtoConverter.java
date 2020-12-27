package com.example.tms.base.converter;

import com.example.tms.base.BaseConverter;
import com.example.tms.base.BaseEntity;
import com.example.tms.base.dto.CrudDto;
import com.example.tms.utility.ConvertUtils;

public interface EntityDtoConverter<E extends BaseEntity, D extends CrudDto<ID>, ID> extends BaseConverter {

    abstract E fromDto(D dto);
    default E fromDto(D dto, Class<E> entityType) {
        return ConvertUtils.convert(dto, entityType);
    }

    E fromDtoForCreate(D dto);

    abstract D fromEntity(E entity);
    default D fromEntity(E entity, Class<D> dtoType) {
        return ConvertUtils.convert(entity, dtoType);
    }

    default void mergeEntity(E target, E source) {
        ConvertUtils.merge(source, target);
    }
}
