package com.example.tms.sample.familytable;

import org.springframework.stereotype.Component;

@Component
public class ParentTableConverter {

    public ParentTableEntity fromDto(ParentTableDto dto) {
        return ParentTableEntity.builder()
                .id(dto.getId())
                .parentField(dto.getParent())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    public ParentTableEntity fromDtoForCreate(ParentTableDto dto) {
        ParentTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    public ParentTableDto fromEntity(ParentTableEntity entity) {
        return ParentTableDto.builder()
                .id(entity.getId())
                .parent(entity.getParentField())
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }

    public void mergeEntity(ParentTableEntity target, ParentTableEntity source) {
        target.setId(source.getId());
        target.setParentField(source.getParentField());
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
