package com.example.tms.sample.singletable;

import com.example.tms.base.converter.EntityDtoConverter;

import org.springframework.stereotype.Component;

@Component
public class SingleTableConverter 
        implements EntityDtoConverter<SingleTableEntity, SingleTableDto, Long> {

    @Override
    public SingleTableEntity fromDto(SingleTableDto dto) {
        return EntityDtoConverter.super.fromDto(dto, SingleTableEntity.class);
    }

    @Override
    public SingleTableEntity fromDtoForCreate(SingleTableDto dto) {
        SingleTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    @Override
    public SingleTableDto fromEntity(SingleTableEntity entity) {
        return EntityDtoConverter.super.fromEntity(entity, SingleTableDto.class);
    }
}
