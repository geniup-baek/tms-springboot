package com.example.tms.samplenonextended.singletable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonExtendedSingleTableRepository extends JpaRepository<NonExtendedSingleTableEntity, Long>  {
    
}
