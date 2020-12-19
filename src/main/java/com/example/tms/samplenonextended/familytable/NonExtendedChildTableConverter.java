package com.example.tms.samplenonextended.familytable;

import org.springframework.stereotype.Component;

@Component
public class NonExtendedChildTableConverter {

    public NonExtendedChildTableEntity fromDto(NonExtendedChildTableDto dto) {
        return NonExtendedChildTableEntity.builder()
                .childId(dto.getChildId())
                .nonForeignkeyParentId(dto.getNonForeignkeyParentId())
                // .foreignkeyParent(foreignkeyParent) // set outside
                .childField(dto.getChild())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    public NonExtendedChildTableEntity fromDtoForCreate(NonExtendedChildTableDto dto) {
        NonExtendedChildTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    public NonExtendedChildTableDto fromEntity(NonExtendedChildTableEntity entity) {

        NonExtendedChildTableDto dto = NonExtendedChildTableDto.builder()
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

    public void mergeEntity(NonExtendedChildTableEntity target, NonExtendedChildTableEntity source) {
        target.setChildId(source.getChildId());
        target.setChildField(source.getChildField());
        target.setNonForeignkeyParentId(source.getNonForeignkeyParentId());
        // target.setForeignkeyParent(source.getForeignkeyParent) // set outside
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
