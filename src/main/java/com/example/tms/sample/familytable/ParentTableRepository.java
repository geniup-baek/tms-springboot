package com.example.tms.sample.familytable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentTableRepository extends CrudRepository<ParentTableEntity, Long> {
    
}
