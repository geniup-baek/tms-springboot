package com.example.tms.sample.familytable;

import com.example.tms.base.converter.EntityDtoConverter;

import org.springframework.stereotype.Component;

@Component
public class ChildTableConverter 
        implements EntityDtoConverter<ChildTableEntity, ChildTableDto, Long> {

    @Override
    public ChildTableEntity fromDto(ChildTableDto dto) {
        return EntityDtoConverter.super.fromDto(dto, ChildTableEntity.class);
        // TODO: extract to Base
        // .foreignkeyParent(foreignkeyParent) // set outside
    }

    @Override
    public ChildTableEntity fromDtoForCreate(ChildTableDto dto) {
        ChildTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    @Override
    public ChildTableDto fromEntity(ChildTableEntity entity) {

        ChildTableDto dto = EntityDtoConverter.super.fromEntity(entity, ChildTableDto.class);

        // TODO: extract to Base
        if (entity.getNonForeignkeyParent() != null) {
            dto.setNonForeignkeyParentParentField(entity.getNonForeignkeyParent().getParentField());
        }                
        if (entity.getForeignkeyParent() != null) {
            dto.setForeignkeyParentId(entity.getForeignkeyParent().getParentId());
            dto.setForeignkeyParentParentField(entity.getForeignkeyParent().getParentField());
        }                

        return dto;
    }
}
