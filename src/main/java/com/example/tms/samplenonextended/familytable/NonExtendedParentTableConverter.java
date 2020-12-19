package com.example.tms.samplenonextended.familytable;

import org.springframework.stereotype.Component;

@Component
public class NonExtendedParentTableConverter {

    public NonExtendedParentTableEntity fromDto(NonExtendedParentTableDto dto) {
        return NonExtendedParentTableEntity.builder()
                .parentId(dto.getParentId())
                .parentField(dto.getParent())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    public NonExtendedParentTableEntity fromDtoForCreate(NonExtendedParentTableDto dto) {
        NonExtendedParentTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    public NonExtendedParentTableDto fromEntity(NonExtendedParentTableEntity entity) {
        return NonExtendedParentTableDto.builder()
                .parentId(entity.getParentId())
                .parent(entity.getParentField())
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }

    public void mergeEntity(NonExtendedParentTableEntity target, NonExtendedParentTableEntity source) {
        target.setParentId(source.getParentId());
        target.setParentField(source.getParentField());
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
