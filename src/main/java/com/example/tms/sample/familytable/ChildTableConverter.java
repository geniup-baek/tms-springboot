package com.example.tms.sample.familytable;

import com.example.tms.base.converter.EntityDtoConverter;

import org.springframework.stereotype.Component;

@Component
public class ChildTableConverter 
        implements EntityDtoConverter<ChildTableEntity, ChildTableDto, Long> {

    @Override
    public ChildTableEntity fromDto(ChildTableDto dto) {
        return ChildTableEntity.builder()
                .childId(dto.getChildId())
                .nonForeignkeyParentId(dto.getNonForeignkeyParentId())
                // .foreignkeyParent(foreignkeyParent) // set outside
                .childField(dto.getChild())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    @Override
    public ChildTableEntity fromDtoForCreate(ChildTableDto dto) {
        ChildTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    @Override
    public ChildTableDto fromEntity(ChildTableEntity entity) {

        ChildTableDto dto = ChildTableDto.builder()
                .childId(entity.getChildId())
                .nonForeignkeyParentId(entity.getNonForeignkeyParentId())
                .child(entity.getChildField())
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();

        if (entity.getNonForeignkeyParent() != null) {
            dto.setNonForeignkeyParentParentField(entity.getNonForeignkeyParent().getParentField());
        }                
        if (entity.getForeignkeyParent() != null) {
            dto.setForeignkeyParentId(entity.getForeignkeyParent().getParentId());
            dto.setForeignkeyParentParentField(entity.getForeignkeyParent().getParentField());
        }                

        return dto;
    }

    @Override
    public void mergeEntity(ChildTableEntity target, ChildTableEntity source) {
        target.setChildId(source.getChildId());
        target.setChildField(source.getChildField());
        target.setNonForeignkeyParentId(source.getNonForeignkeyParentId());
        // target.setForeignkeyParent(source.getForeignkeyParent) // set outside
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
