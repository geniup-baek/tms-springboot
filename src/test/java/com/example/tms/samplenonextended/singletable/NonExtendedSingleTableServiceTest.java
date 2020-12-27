package com.example.tms.samplenonextended.singletable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.example.tms.samplenonextended.singletable"})
@DataJpaTest
public class NonExtendedSingleTableServiceTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NonExtendedSingleTableService service;

    @Autowired
    private NonExtendedSingleTableRepository repository;

    @Nested
    public class Create {
        @Test
        public void when_create_then_created() {
    
            NonExtendedSingleTableDto dto = NonExtendedSingleTableDto.builder()
                    .requiredString("requiredString")
                    .build();
    
            Long id = service.create(dto);
    
            assertThat(id).isEqualTo(1L);
        }      
    }

    @Nested
    public class Read {
        @Test
        public void when_read_then_find_dto() {
    
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("requiredStringField")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("requiredStringField2")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
    
            NonExtendedSingleTableDto dto = service.read(entity2.getSingleId());
    
            assertThat(dto.getSingleId()).isEqualTo(entity2.getSingleId());
            assertThat(dto.getRequiredString()).isEqualTo(entity2.getRequiredStringField());
        }   
    }

    @Nested
    public class Update {
        @Test
        public void when_update_then_updated() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
    
            int originVersion = entity2.getVersion();
            NonExtendedSingleTableDto updateDto = NonExtendedSingleTableDto.builder()
                    .singleId(entity2.getSingleId())
                    .requiredString("Updated Java Sample")
                    .version(originVersion)
                    .deleted(false)
                    .build();
    
            NonExtendedSingleTableDto updatedDto = service.update(entity2.getSingleId(), updateDto);
            assertThat(updatedDto.getSingleId()).isEqualTo(entity2.getSingleId());
            assertThat(updatedDto.getRequiredString()).isEqualTo(updateDto.getRequiredString());
            assertThat(updatedDto.getVersion()).isEqualTo(originVersion + 1);
    
            NonExtendedSingleTableEntity checkEntity = repository.findById(entity2.getSingleId()).get();
            assertThat(checkEntity.getSingleId()).isEqualTo(entity2.getSingleId());
            assertThat(checkEntity.getRequiredStringField()).isEqualTo(updateDto.getRequiredString());
            assertThat(checkEntity.getVersion()).isEqualTo(originVersion + 1);
        }
    }

    @Nested
    public class Delete {
        @Test
        public void when_delete_then_deleted() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);

            service.delete(entity2.getSingleId(), entity2.getVersion());
    
            Iterable<NonExtendedSingleTableEntity> entities = repository.findAll();
    
            assertThat(entities).hasSize(1).contains(entity1);            
        }

        @Test
        public void when_deleteList_then_deleted() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
            NonExtendedSingleTableEntity entity3 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Data JPA Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity3);

            List<SimpleEntry<Long, Integer>> entryList = new ArrayList<>();
            entryList.add(new SimpleEntry<Long, Integer>(entity2.getSingleId(), entity2.getVersion()));
            entryList.add(new SimpleEntry<Long, Integer>(entity3.getSingleId(), entity3.getVersion()));

            service.deleteList(entryList);
    
            Iterable<NonExtendedSingleTableEntity> entities = repository.findAll();
    
            assertThat(entities).hasSize(1).contains(entity1);            
        }

        @Test
        public void when_deleteLogical_then_logically_deleted() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);

            int originVersion = entity2.getVersion();
            service.deleteLogical(entity2.getSingleId(), originVersion);
    
            NonExtendedSingleTableEntity checkEntity = repository.findById(entity2.getSingleId()).get();
            assertThat(checkEntity.getSingleId()).isEqualTo(entity2.getSingleId());
            assertThat(checkEntity.getDeleted()).isEqualTo(Boolean.TRUE);
            assertThat(checkEntity.getVersion()).isEqualTo(originVersion + 1);
        }

        @Test
        public void when_deleteListLogical_then_logically_deleted() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
            NonExtendedSingleTableEntity entity3 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Data JPA Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity3);

            int originVersion2 = entity2.getVersion();
            int originVersion3 = entity3.getVersion();

            List<SimpleEntry<Long, Integer>> entryList = new ArrayList<>();
            entryList.add(new SimpleEntry<Long, Integer>(entity2.getSingleId(), entity2.getVersion()));
            entryList.add(new SimpleEntry<Long, Integer>(entity3.getSingleId(), entity3.getVersion()));
            service.deleteListLogical(entryList);
    
            NonExtendedSingleTableEntity checkEntity2 = repository.findById(entity2.getSingleId()).get();
            assertThat(checkEntity2.getSingleId()).isEqualTo(entity2.getSingleId());
            assertThat(checkEntity2.getDeleted()).isEqualTo(Boolean.TRUE);
            assertThat(checkEntity2.getVersion()).isEqualTo(originVersion2 + 1);

            NonExtendedSingleTableEntity checkEntity3 = repository.findById(entity3.getSingleId()).get();
            assertThat(checkEntity3.getSingleId()).isEqualTo(entity3.getSingleId());
            assertThat(checkEntity3.getDeleted()).isEqualTo(Boolean.TRUE);
            assertThat(checkEntity3.getVersion()).isEqualTo(originVersion3 + 1);
        }        
    }

    @Nested
    public class Search {
        @Test
        public void when_empty_then_found_no_dtos() {
            NonExtendedSingleTableSearchCondition searchCondition = NonExtendedSingleTableSearchCondition.builder().build();
            List<NonExtendedSingleTableDto> dtos = service.search(searchCondition, 100);
            assertThat(dtos).isEmpty();
        }
    
        @Test
        public void when_search_by_empty_conditions_then_found_all_dtos() {
    
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("requiredStringField")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("requiredStringField2")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
    
            NonExtendedSingleTableSearchCondition searchCondition = NonExtendedSingleTableSearchCondition.builder().build();
            List<NonExtendedSingleTableDto> dtos = service.search(searchCondition, 100);
    
            assertThat(dtos).hasSize(2); // .contains(entity1, entity2);
        }    
    
        @Test
        public void when_search_by_equals_condition_then_found_dtos_matching() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .codeField("code1")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .codeField("code2")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
    
            NonExtendedSingleTableSearchCondition searchCondition = NonExtendedSingleTableSearchCondition.builder()
                    .code("code2")
                    .build();
            List<NonExtendedSingleTableDto> dtos = service.search(searchCondition, 100);
    
            assertThat(dtos).hasSize(1); // .contains(entity2);
            assertThat(dtos.get(0).getCode()).isEqualTo(searchCondition.getCode());
        }    

        @Test
        public void when_search_by_contains_condition_then_found_dtos_matching() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
            NonExtendedSingleTableEntity entity3 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Data JPA Sample")
                    .deleted(false)
                    .build();
            entityManager.persist(entity3);
    
            NonExtendedSingleTableSearchCondition searchCondition = NonExtendedSingleTableSearchCondition.builder()
                    .requiredString("ring")
                    .build();
            List<NonExtendedSingleTableDto> dtos = service.search(searchCondition, 100);
    
            assertThat(dtos).hasSize(2); // .contains(entity1, entity3);
            for (NonExtendedSingleTableDto dto : dtos) {
                assertThat(dto.getRequiredString()).contains(searchCondition.getRequiredString());
            }
        }

        @Test
        public void when_search_by_complex_condition_then_found_dtos_matching() {
            NonExtendedSingleTableEntity entity1 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Boot Sample")
                    .codeField("code1")
                    .deleted(false)
                    .build();
            entityManager.persist(entity1);
            NonExtendedSingleTableEntity entity2 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Java Sample")
                    .codeField("code2")
                    .deleted(false)
                    .build();
            entityManager.persist(entity2);
            NonExtendedSingleTableEntity entity3 = NonExtendedSingleTableEntity.builder()
                    .requiredStringField("Spring Data JPA Sample")
                    .codeField("code3")
                    .deleted(false)
                    .build();
            entityManager.persist(entity3);
    
            NonExtendedSingleTableSearchCondition searchCondition = NonExtendedSingleTableSearchCondition.builder()
                    .code("code3")
                    .requiredString("ring")
                    .build();
            List<NonExtendedSingleTableDto> dtos = service.search(searchCondition, 100);
    
            assertThat(dtos).hasSize(1); // .contains(entity3);
            assertThat(dtos.get(0).getCode()).isEqualTo(searchCondition.getCode());
            assertThat(dtos.get(0).getRequiredString()).contains(searchCondition.getRequiredString());
        }        
    }
}
