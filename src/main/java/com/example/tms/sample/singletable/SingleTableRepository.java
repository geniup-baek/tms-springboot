package com.example.tms.sample.singletable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleTableRepository extends JpaRepository<SingleTableEntity, Long>  {
    
}
