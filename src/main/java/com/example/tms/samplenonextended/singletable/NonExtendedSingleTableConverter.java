package com.example.tms.samplenonextended.singletable;

import org.springframework.stereotype.Component;

@Component
public class NonExtendedSingleTableConverter {

    public NonExtendedSingleTableEntity fromDto(NonExtendedSingleTableDto dto) {
        return NonExtendedSingleTableEntity.builder()
                .singleId(dto.getSingleId())
                .requiredStringField(dto.getRequiredString())
                .codeField(dto.getCode())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    public NonExtendedSingleTableEntity fromDtoForCreate(NonExtendedSingleTableDto dto) {
        NonExtendedSingleTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    public NonExtendedSingleTableDto fromEntity(NonExtendedSingleTableEntity entity) {
        return NonExtendedSingleTableDto.builder()
                .singleId(entity.getSingleId())
                .requiredString(entity.getRequiredStringField())
                .code(entity.getCodeField())
                .name(null) // none-entity-field
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }

    public void mergeEntity(NonExtendedSingleTableEntity target, NonExtendedSingleTableEntity source) {
        target.setSingleId(source.getSingleId());
        target.setRequiredStringField(source.getRequiredStringField());
        target.setCodeField(source.getCodeField());
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
