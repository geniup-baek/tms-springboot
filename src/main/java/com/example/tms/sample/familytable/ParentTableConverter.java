package com.example.tms.sample.familytable;

import com.example.tms.base.converter.EntityDtoConverter;

import org.springframework.stereotype.Component;

@Component
public class ParentTableConverter
        implements EntityDtoConverter<ParentTableEntity, ParentTableDto, Long> {

    @Override
    public ParentTableEntity fromDto(ParentTableDto dto) {
        return ParentTableEntity.builder()
                .parentId(dto.getParentId())
                .parentField(dto.getParent())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    @Override
    public ParentTableEntity fromDtoForCreate(ParentTableDto dto) {
        ParentTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    @Override
    public ParentTableDto fromEntity(ParentTableEntity entity) {
        return ParentTableDto.builder()
                .parentId(entity.getParentId())
                .parent(entity.getParentField())
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }

    @Override
    public void mergeEntity(ParentTableEntity target, ParentTableEntity source) {
        target.setParentId(source.getParentId());
        target.setParentField(source.getParentField());
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
