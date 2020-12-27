package com.example.tms.sample.familytable;

import com.example.tms.base.converter.EntityDtoConverter;

import org.springframework.stereotype.Component;

@Component
public class ParentTableConverter
        implements EntityDtoConverter<ParentTableEntity, ParentTableDto, Long> {

    @Override
    public ParentTableEntity fromDto(ParentTableDto dto) {
        return EntityDtoConverter.super.fromDto(dto, ParentTableEntity.class);
    }

    @Override
    public ParentTableEntity fromDtoForCreate(ParentTableDto dto) {
        ParentTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    @Override
    public ParentTableDto fromEntity(ParentTableEntity entity) {
        return EntityDtoConverter.super.fromEntity(entity, ParentTableDto.class);
    }
}
