package com.example.tms.sample.singletable;

import org.springframework.stereotype.Component;

@Component
public class SingleTableConverter {

    public SingleTableEntity fromDto(SingleTableDto dto) {
        return SingleTableEntity.builder()
                .id(dto.getId())
                .requiredStringField(dto.getRequiredString())
                .codeField(dto.getCode())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    public SingleTableEntity fromDtoForCreate(SingleTableDto dto) {
        SingleTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    public SingleTableDto fromEntity(SingleTableEntity entity) {
        return SingleTableDto.builder()
                .id(entity.getId())
                .requiredString(entity.getRequiredStringField())
                .code(entity.getCodeField())
                .name(null) // none-entity-field
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }

    public void mergeEntity(SingleTableEntity target, SingleTableEntity source) {
        target.setId(source.getId());
        target.setRequiredStringField(source.getRequiredStringField());
        target.setCodeField(source.getCodeField());
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
