package com.example.tms.sample.familytable;

import org.springframework.data.repository.CrudRepository;

public interface ChildTableRepository extends CrudRepository<ChildTableEntity, Long> {
    Iterable<ChildTableEntity> findAllByForeignkeyParentId(Long foreignkeyParentId);
}
