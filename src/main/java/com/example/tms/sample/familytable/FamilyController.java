package com.example.tms.sample.familytable;

import com.example.tms.base.BaseService;
import com.example.tms.base.controller.CrudController;
import com.example.tms.base.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample-families")
public class FamilyController implements
        CrudController<ParentTableEntity, ParentTableDto, ParentTableRepository, ParentTableConverter, FamilySearchCondition, Long> {

    private FamilyService service;

    @Autowired
    public FamilyController(FamilyService service) {
        this.service = service;
    }

    @Override
    public BaseService getService() {
        return service;
    }

    @Override
    public CrudService<ParentTableEntity, ParentTableDto, ParentTableRepository, ParentTableConverter, FamilySearchCondition, Long> getCrudService() {
        return service;
    }

    @Override
    public Class<FamilySearchCondition> getSearchControllerType() {
        return FamilySearchCondition.class;
    }    
}
