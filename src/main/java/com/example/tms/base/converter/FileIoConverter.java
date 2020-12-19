package com.example.tms.base.converter;

import com.example.tms.base.BaseConverter;
import com.example.tms.base.dto.CrudDto;
import com.example.tms.base.dto.FileIoDto;

public interface FileIoConverter<F extends FileIoDto<ID>, D extends CrudDto<ID>, ID> extends BaseConverter {

    F fromDto(D dto);
    D fromFileIoDto(F fileIoDto);
}
