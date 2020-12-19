package com.example.tms.samplenonextended.familytable;

import org.springframework.data.repository.CrudRepository;

public interface NonExtendedParentTableRepository extends CrudRepository<NonExtendedParentTableEntity, Long> {
    
}
