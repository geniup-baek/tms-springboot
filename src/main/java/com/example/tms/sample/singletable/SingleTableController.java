package com.example.tms.sample.singletable;

import com.example.tms.base.BaseService;
import com.example.tms.base.controller.CrudController;
import com.example.tms.base.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample-singles")
public class SingleTableController implements CrudController<SingleTableEntity, SingleTableDto, SingleTableRepository, SingleTableConverter, SingleTableSearchCondition, Long> {

    private SingleTableService service;

    @Autowired
    public SingleTableController(SingleTableService service) {
        this.service = service;
    }

    @Override
    public BaseService getService() {
        return service;
    }

    @Override
    public Class<SingleTableSearchCondition> getSearchControllerType() {
        return SingleTableSearchCondition.class;
    }

    @Override
    public CrudService<SingleTableEntity, SingleTableDto, SingleTableRepository, SingleTableConverter, SingleTableSearchCondition, Long> getCrudService() {
        return service;
    }
}
