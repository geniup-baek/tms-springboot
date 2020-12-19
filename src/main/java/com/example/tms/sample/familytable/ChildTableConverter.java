package com.example.tms.sample.familytable;

import org.springframework.stereotype.Component;

@Component
public class ChildTableConverter {

    public ChildTableEntity fromDto(ChildTableDto dto) {
        return ChildTableEntity.builder()
                .id(dto.getId())
                .nonForeignkeyParentId(dto.getNonForeignkeyParentId())
                // .foreignkeyParent(foreignkeyParent) // set outside
                .childField(dto.getChild())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    public ChildTableEntity fromDtoForCreate(ChildTableDto dto) {
        ChildTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    public ChildTableDto fromEntity(ChildTableEntity entity) {

        ChildTableDto dto = ChildTableDto.builder()
                .id(entity.getId())
                .nonForeignkeyParentId(entity.getNonForeignkeyParentId())
                .child(entity.getChildField())
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();

        if (entity.getNonForeignkeyParent() != null) {
            dto.setNonForeignkeyParentParentField(entity.getNonForeignkeyParent().getParentField());
        }                
        if (entity.getForeignkeyParent() != null) {
            dto.setForeignkeyParentId(entity.getForeignkeyParent().getId());
            dto.setForeignkeyParentParentField(entity.getForeignkeyParent().getParentField());
        }                

        return dto;
    }

    public void mergeEntity(ChildTableEntity target, ChildTableEntity source) {
        target.setId(source.getId());
        target.setChildField(source.getChildField());
        target.setNonForeignkeyParentId(source.getNonForeignkeyParentId());
        // target.setForeignkeyParent(source.getForeignkeyParent) // set outside
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
