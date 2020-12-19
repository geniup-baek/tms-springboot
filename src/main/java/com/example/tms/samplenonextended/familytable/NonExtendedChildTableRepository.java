package com.example.tms.samplenonextended.familytable;

import org.springframework.data.repository.CrudRepository;

public interface NonExtendedChildTableRepository extends CrudRepository<NonExtendedChildTableEntity, Long> {
    Iterable<NonExtendedChildTableEntity> findAllByForeignkeyParentParentId(Long foreignkeyParentId);
    void deleteByForeignkeyParentParentId(Long foreignkeyParentId);
}
