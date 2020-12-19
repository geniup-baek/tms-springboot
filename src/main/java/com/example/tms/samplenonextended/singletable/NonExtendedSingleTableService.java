package com.example.tms.samplenonextended.singletable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;

import javax.persistence.EntityManager;

import com.example.tms.define.Const;
import com.example.tms.error.ApplicationException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class NonExtendedSingleTableService {

    private JPAQueryFactory queryFactory;

    private NonExtendedSingleTableRepository repository;

    private NonExtendedSingleTableConverter converter;

    @Autowired
    public NonExtendedSingleTableService(EntityManager entityManager, NonExtendedSingleTableRepository repository, NonExtendedSingleTableConverter converter) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional
    public NonExtendedSingleTableDto read(Long id) {
        NonExtendedSingleTableSearchCondition searchCondition = NonExtendedSingleTableSearchCondition.builder().build();
        searchCondition.setId(id);

        List<NonExtendedSingleTableDto> resultList = search(searchCondition, Integer.MAX_VALUE);
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }

    @Transactional
    public Long create(NonExtendedSingleTableDto dto) {

        NonExtendedSingleTableEntity entity = converter.fromDtoForCreate(dto);
        NonExtendedSingleTableEntity createdEntity = repository.save(entity);

        return createdEntity.getSingleId();
    }

    @Transactional
    public NonExtendedSingleTableDto update(Long id, NonExtendedSingleTableDto dto) {

        NonExtendedSingleTableDto updatedDto = null;

        Optional<NonExtendedSingleTableEntity> optionalEntity = repository.findById(id);
        if (optionalEntity.isPresent()) {
            NonExtendedSingleTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(dto.getVersion())) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }

            converter.mergeEntity(entity, converter.fromDto(dto));

            NonExtendedSingleTableEntity updatedEntity = repository.save(entity);
            updatedDto = converter.fromEntity(updatedEntity);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }

        return updatedDto;
    }

    @Transactional
    public void delete(Long id, Integer version) {
        Optional<NonExtendedSingleTableEntity> optionalEntity = repository.findById(id);

        if (optionalEntity.isPresent()) {
            NonExtendedSingleTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            repository.deleteById(id);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }

    @Transactional
    public void deleteLogical(Long id, Integer version) {

        Optional<NonExtendedSingleTableEntity> optionalEntity = repository.findById(id);
        if (optionalEntity.isPresent()) {
            NonExtendedSingleTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            entity.setDeleted(true);
            repository.save(entity);

        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }

    @Transactional
    public void deleteList(List<SimpleEntry<Long, Integer>> entryList) {
        for (SimpleEntry<Long, Integer> entry : entryList) {
            delete(entry.getKey(), entry.getValue());
        }
    }

    @Transactional
    public void deleteListLogical(List<SimpleEntry<Long, Integer>> entryList) {
        for (SimpleEntry<Long, Integer> entry : entryList) {
            deleteLogical(entry.getKey(), entry.getValue());
        }
    }

    @Transactional
	public List<NonExtendedSingleTableDto> search(NonExtendedSingleTableSearchCondition searchCondition, int size) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchCondition.getId() != null) {
            builder.and(QNonExtendedSingleTableEntity.nonExtendedSingleTableEntity.singleId.eq(searchCondition.getId()));
        }

        if (StringUtils.hasText(searchCondition.getRequiredString())) {
            builder.and(QNonExtendedSingleTableEntity.nonExtendedSingleTableEntity.requiredStringField.contains(searchCondition.getRequiredString()));
        }

        if (StringUtils.hasText(searchCondition.getCode())) {
            builder.and(QNonExtendedSingleTableEntity.nonExtendedSingleTableEntity.codeField.contains(searchCondition.getCode()));
        }

        List<NonExtendedSingleTableDto> dtoList = new ArrayList<>();

        List<NonExtendedSingleTableEntity> resultList = queryFactory
                .select(QNonExtendedSingleTableEntity.nonExtendedSingleTableEntity)
                .from(QNonExtendedSingleTableEntity.nonExtendedSingleTableEntity)
                .where(builder)
                .limit(size)
                .fetch();

        resultList.stream().forEach(entity -> dtoList.add(converter.fromEntity(entity)));

		return dtoList;
	}
}
