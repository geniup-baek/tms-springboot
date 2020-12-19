package com.example.tms.sample.singletable;

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
public class SingleTableService {

    private JPAQueryFactory queryFactory;

    private SingleTableRepository repository;

    private SingleTableConverter converter;

    @Autowired
    public SingleTableService(EntityManager entityManager, SingleTableRepository repository, SingleTableConverter converter) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional
    public SingleTableDto read(Long id) {
        SingleTableSearchCondition searchCondition = SingleTableSearchCondition.builder().build();
        searchCondition.setId(id);

        List<SingleTableDto> resultList = search(searchCondition, Integer.MAX_VALUE);
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }

    @Transactional
    public Long create(SingleTableDto dto) {

        SingleTableEntity entity = converter.fromDtoForCreate(dto);
        SingleTableEntity createdEntity = repository.save(entity);

        return createdEntity.getId();
    }

    @Transactional
    public SingleTableDto update(Long id, SingleTableDto dto) {

        SingleTableDto updatedDto = null;

        Optional<SingleTableEntity> optionalEntity = repository.findById(id);
        if (optionalEntity.isPresent()) {
            SingleTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(dto.getVersion())) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }

            converter.mergeEntity(entity, converter.fromDto(dto));

            SingleTableEntity updatedEntity = repository.save(entity);
            updatedDto = converter.fromEntity(updatedEntity);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }

        return updatedDto;
    }

    @Transactional
    public void delete(Long id, Integer version) {
        Optional<SingleTableEntity> optionalEntity = repository.findById(id);

        if (optionalEntity.isPresent()) {
            SingleTableEntity entity = optionalEntity.get();
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

        Optional<SingleTableEntity> optionalEntity = repository.findById(id);
        if (optionalEntity.isPresent()) {
            SingleTableEntity entity = optionalEntity.get();
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
	public List<SingleTableDto> search(SingleTableSearchCondition searchCondition, int size) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchCondition.getId() != null) {
            builder.and(QSingleTableEntity.singleTableEntity.id.eq(searchCondition.getId()));
        }

        if (StringUtils.hasText(searchCondition.getRequiredString())) {
            builder.and(QSingleTableEntity.singleTableEntity.requiredStringField.contains(searchCondition.getRequiredString()));
        }

        if (StringUtils.hasText(searchCondition.getCode())) {
            builder.and(QSingleTableEntity.singleTableEntity.codeField.contains(searchCondition.getCode()));
        }

        List<SingleTableDto> dtoList = new ArrayList<>();

        List<SingleTableEntity> resultList = queryFactory
                .select(QSingleTableEntity.singleTableEntity)
                .from(QSingleTableEntity.singleTableEntity)
                .where(builder)
                .limit(size)
                .fetch();

        resultList.stream().forEach(entity -> dtoList.add(converter.fromEntity(entity)));

		return dtoList;
	}
}
