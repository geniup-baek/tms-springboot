package com.example.tms.sample.singletable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.example.tms.base.service.CrudService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SingleTableService
        implements CrudService<SingleTableEntity, SingleTableDto, SingleTableRepository, SingleTableConverter, SingleTableSearchCondition, Long> {

    private JPAQueryFactory queryFactory;

    private SingleTableRepository repository;

    private SingleTableConverter converter;

    @Autowired
    public SingleTableService(EntityManager entityManager, SingleTableRepository repository, SingleTableConverter converter) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public SingleTableSearchCondition getIdSearchCondition(Long id) {
        return SingleTableSearchCondition.builder().singleTableId(id).build();
    }

    @Override
    public SingleTableRepository getRepository() {
        return repository;
    }

    @Override
    public SingleTableConverter getConverter() {
        return converter;
    }

    @Override
	public List<SingleTableDto> search(SingleTableSearchCondition searchCondition, int size, boolean withDetail) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchCondition.getId() != null) {
            builder.and(QSingleTableEntity.singleTableEntity.singleId.eq(searchCondition.getId()));
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
