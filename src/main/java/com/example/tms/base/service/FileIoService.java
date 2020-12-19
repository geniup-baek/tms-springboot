package com.example.tms.base.service;

import com.example.tms.base.BaseSearchCondition;
import com.example.tms.base.BaseService;
import com.example.tms.base.dto.FileIoDto;

public interface FileIoService<D extends FileIoDto<ID>, C extends BaseSearchCondition<ID>, ID> extends BaseService {
    String getCsv(C searchCondition, int size);
}

