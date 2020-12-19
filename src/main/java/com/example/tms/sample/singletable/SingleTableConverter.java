package com.example.tms.sample.singletable;

import com.example.tms.base.converter.EntityDtoConverter;

import org.springframework.stereotype.Component;

@Component
public class SingleTableConverter 
        implements EntityDtoConverter<SingleTableEntity, SingleTableDto, Long> {

    @Override
    public SingleTableEntity fromDto(SingleTableDto dto) {
        return SingleTableEntity.builder()
                .singleId(dto.getSingleId())
                .requiredStringField(dto.getRequiredString())
                .codeField(dto.getCode())
                .version(dto.getVersion())
                .deleted(dto.getDeleted())
                .build();
    }

    @Override
    public SingleTableEntity fromDtoForCreate(SingleTableDto dto) {
        SingleTableEntity entity = fromDto(dto);
        entity.setForCreate();
        return entity;
    }

    @Override
    public SingleTableDto fromEntity(SingleTableEntity entity) {
        return SingleTableDto.builder()
                .singleId(entity.getSingleId())
                .requiredString(entity.getRequiredStringField())
                .code(entity.getCodeField())
                .name(null) // none-entity-field
                .version(entity.getVersion())
                .deleted(entity.getDeleted())
                .build();
    }

    @Override
    public void mergeEntity(SingleTableEntity target, SingleTableEntity source) {
        target.setSingleId(source.getSingleId());
        target.setRequiredStringField(source.getRequiredStringField());
        target.setCodeField(source.getCodeField());
        // target.setVersion(source.getVersion()); // merge-ignore
        target.setDeleted(source.getDeleted());
    }
}
