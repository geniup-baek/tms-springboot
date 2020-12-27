package com.example.tms.sample.familytable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.example.tms.base.service.CrudService;
import com.example.tms.define.Const;
import com.example.tms.error.ApplicationException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class FamilyService
        implements CrudService<ParentTableEntity, ParentTableDto, ParentTableRepository, ParentTableConverter, FamilySearchCondition, Long> {

    private JPAQueryFactory queryFactory;
    private ParentTableRepository parentRepository;
    private ChildTableRepository childRepository;
    private ParentTableConverter parentConverter;
    private ChildTableConverter childConverter;

    @Autowired
    public FamilyService(EntityManager entityManager, ParentTableRepository parentRepository, ChildTableRepository childRepository, ParentTableConverter parentConverter, ChildTableConverter childConverter) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.parentConverter = parentConverter;
        this.childConverter = childConverter;
    }

    @Override
    public FamilySearchCondition getIdSearchCondition(Long id) {
        return FamilySearchCondition.builder().parentId(id).build();
    }

    @Override
    public ParentTableRepository getRepository() {
        return parentRepository;
    }

    @Override
    public ParentTableConverter getConverter() {
        return parentConverter;
    }

    @Override
    @Transactional
    public Long create(ParentTableDto dto) {

        ParentTableEntity parentEntity = parentConverter.fromDtoForCreate(dto);
        ParentTableEntity createdParentEntity = parentRepository.save(parentEntity);

        dto.children.stream().forEach(childDto -> {

            ChildTableEntity childEntity = childConverter.fromDtoForCreate(childDto);
            childEntity.setForeignkeyParent(createdParentEntity);

            childRepository.save(childEntity);
        });

        return createdParentEntity.getParentId();
    }

    @Override
    @Transactional
    public ParentTableDto update(Long id, ParentTableDto dto) {

        ParentTableDto updatedDto = null;

        Optional<ParentTableEntity> optionalParentEntity = parentRepository.findById(id);
        if (optionalParentEntity.isPresent()) {
            ParentTableEntity parentEntity = optionalParentEntity.get();
            if (!parentEntity.getVersion().equals(dto.getVersion())) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            parentConverter.mergeEntity(parentEntity, parentConverter.fromDto(dto));
            ParentTableEntity updatedParentEntity = parentRepository.save(parentEntity);

            dto.children.stream().forEach(childDto -> {
                if (childDto.getExecuteType() != null) {

                    if (childDto.getExecuteType().equals("C")) {
                        ChildTableEntity childEntity = childConverter.fromDtoForCreate(childDto);
                        childEntity.setForeignkeyParent(updatedParentEntity);
                        childRepository.save(childEntity);
                    } else if (childDto.getExecuteType().equals("U")) {
                        Optional<ChildTableEntity> optionalChildEntity = childRepository.findById(childDto.getChildId());
                        if (optionalChildEntity.isPresent()) {
                            ChildTableEntity childEntity = optionalChildEntity.get();
                            if (!childEntity.getVersion().equals(childDto.getVersion())) {
                                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
                            }
                            childConverter.mergeEntity(childEntity, childConverter.fromDto(childDto));
                            childEntity.setForeignkeyParent(updatedParentEntity);
                            childRepository.save(childEntity);
                        } else {
                            throw new ApplicationException(Const.Message.NOT_FOUND);
                        }
                    } else if (childDto.getExecuteType().equals("D")) {
                        Optional<ChildTableEntity> optionalChildEntity = childRepository.findById(childDto.getChildId());
                        if (optionalChildEntity.isPresent()) {
                            ChildTableEntity childEntity = optionalChildEntity.get();
                            if (!childEntity.getVersion().equals(childDto.getVersion())) {
                                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
                            }
                            childEntity.setDeleted(true);
                            childRepository.save(childEntity);
                        } else {
                            throw new ApplicationException(Const.Message.NOT_FOUND);
                        }
                    }
                }
            });

            updatedDto = read(updatedParentEntity.getParentId());
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }

        return updatedDto;
    }

    @Override
    @Transactional
    public void delete(Long id, Integer version) {
        Optional<ParentTableEntity> optionalEntity = parentRepository.findById(id);

        if (optionalEntity.isPresent()) {
            ParentTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            // delete children
            childRepository.deleteByForeignkeyParentParentId(id);

            parentRepository.deleteById(id);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteLogical(Long id, Integer version) {

        Optional<ParentTableEntity> optionalEntity = parentRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ParentTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }

            // delete children
            childRepository.findAllByForeignkeyParentParentId(id).forEach(childEntity -> {
                childEntity.setDeleted(true);
                childRepository.save(childEntity);
            });

            entity.setDeleted(true);
            parentRepository.save(entity);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }

    @Override
    @Transactional
	public List<ParentTableDto> search(FamilySearchCondition searchCondition, int size, boolean withDetail) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchCondition.getId() != null) {
            builder.and(QParentTableEntity.parentTableEntity.parentId.eq(searchCondition.getId()));
        }

        if (StringUtils.hasText(searchCondition.getParent())) {
            builder.and(QParentTableEntity.parentTableEntity.parentField.contains(searchCondition.getParent()));
        }

        List<ParentTableDto> dtoList = new ArrayList<>();

        List<ParentTableEntity> resultList = queryFactory
                .select(QParentTableEntity.parentTableEntity)
                .from(QParentTableEntity.parentTableEntity)
                .where(builder)
                .limit(size)
                .fetch();

        resultList.stream().forEach(parentEntity -> {
            ParentTableDto dto = parentConverter.fromEntity(parentEntity);
            if (withDetail) {
                dto.children.addAll(searchChild(parentEntity.getParentId()));
            }

            dtoList.add(dto);
        });

		return dtoList;
    }
    
    @Transactional
	public List<ChildTableDto> searchChild(Long parentId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QChildTableEntity.childTableEntity.foreignkeyParent.parentId.eq(parentId));

        List<ChildTableDto> dtoList = new ArrayList<>();

        List<ChildTableEntity> resultList = queryFactory
                .select(QChildTableEntity.childTableEntity)
                .from(QChildTableEntity.childTableEntity)
                .where(builder)
                .fetch();

        resultList.stream().forEach(entity -> {
            ChildTableDto dto = childConverter.fromEntity(entity);
            dtoList.add(dto);
        });

		return dtoList;
	}


}
