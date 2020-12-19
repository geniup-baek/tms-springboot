package com.example.tms.samplenonextended.familytable;

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
public class NonExtendedFamilyService {

    private JPAQueryFactory queryFactory;
    private NonExtendedParentTableRepository parentRepository;
    private NonExtendedChildTableRepository childRepository;
    private NonExtendedParentTableConverter parentConverter;
    private NonExtendedChildTableConverter childConverter;

    @Autowired
    public NonExtendedFamilyService(EntityManager entityManager, NonExtendedParentTableRepository parentRepository, NonExtendedChildTableRepository childRepository, NonExtendedParentTableConverter parentConverter, NonExtendedChildTableConverter childConverter) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.parentConverter = parentConverter;
        this.childConverter = childConverter;
    }

    @Transactional
    public NonExtendedParentTableDto read(Long id) {
        NonExtendedFamilySearchCondition searchCondition = NonExtendedFamilySearchCondition.builder().build();
        searchCondition.setId(id);

        List<NonExtendedParentTableDto> resultList = search(searchCondition, Integer.MAX_VALUE, true);
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }

    @Transactional
    public Long create(NonExtendedParentTableDto dto) {

        NonExtendedParentTableEntity parentEntity = parentConverter.fromDtoForCreate(dto);
        NonExtendedParentTableEntity createdParentEntity = parentRepository.save(parentEntity);

        dto.children.stream().forEach(childDto -> {

            NonExtendedChildTableEntity childEntity = childConverter.fromDtoForCreate(childDto);
            childEntity.setForeignkeyParent(createdParentEntity);

            childRepository.save(childEntity);
        });

        return createdParentEntity.getParentId();
    }

    @Transactional
    public NonExtendedParentTableDto update(Long id, NonExtendedParentTableDto dto) {

        NonExtendedParentTableDto updatedDto = null;

        Optional<NonExtendedParentTableEntity> optionalParentEntity = parentRepository.findById(id);
        if (optionalParentEntity.isPresent()) {
            NonExtendedParentTableEntity parentEntity = optionalParentEntity.get();
            if (!parentEntity.getVersion().equals(dto.getVersion())) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            parentConverter.mergeEntity(parentEntity, parentConverter.fromDto(dto));
            NonExtendedParentTableEntity updatedParentEntity = parentRepository.save(parentEntity);

            dto.children.stream().forEach(childDto -> {
                if (childDto.getExecuteType() != null) {

                    if (childDto.getExecuteType().equals("C")) {
                        NonExtendedChildTableEntity childEntity = childConverter.fromDtoForCreate(childDto);
                        childEntity.setForeignkeyParent(updatedParentEntity);
                        childRepository.save(childEntity);
                    } else if (childDto.getExecuteType().equals("U")) {
                        Optional<NonExtendedChildTableEntity> optionalChildEntity = childRepository.findById(childDto.getChildId());
                        if (optionalChildEntity.isPresent()) {
                            NonExtendedChildTableEntity childEntity = optionalChildEntity.get();
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
                        Optional<NonExtendedChildTableEntity> optionalChildEntity = childRepository.findById(childDto.getChildId());
                        if (optionalChildEntity.isPresent()) {
                            NonExtendedChildTableEntity childEntity = optionalChildEntity.get();
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

    @Transactional
    public void delete(Long id, Integer version) {
        Optional<NonExtendedParentTableEntity> optionalEntity = parentRepository.findById(id);

        if (optionalEntity.isPresent()) {
            NonExtendedParentTableEntity entity = optionalEntity.get();
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

    @Transactional
    public void deleteLogical(Long id, Integer version) {

        Optional<NonExtendedParentTableEntity> optionalEntity = parentRepository.findById(id);
        if (optionalEntity.isPresent()) {
            NonExtendedParentTableEntity entity = optionalEntity.get();
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
	public List<NonExtendedParentTableDto> search(NonExtendedFamilySearchCondition searchCondition, int size, boolean withDetail) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchCondition.getId() != null) {
            builder.and(QNonExtendedParentTableEntity.nonExtendedParentTableEntity.parentId.eq(searchCondition.getId()));
        }

        if (StringUtils.hasText(searchCondition.getParent())) {
            builder.and(QNonExtendedParentTableEntity.nonExtendedParentTableEntity.parentField.contains(searchCondition.getParent()));
        }

        List<NonExtendedParentTableDto> dtoList = new ArrayList<>();

        List<NonExtendedParentTableEntity> resultList = queryFactory
                .select(QNonExtendedParentTableEntity.nonExtendedParentTableEntity)
                .from(QNonExtendedParentTableEntity.nonExtendedParentTableEntity)
                .where(builder)
                .limit(size)
                .fetch();

        resultList.stream().forEach(parentEntity -> {
            NonExtendedParentTableDto dto = parentConverter.fromEntity(parentEntity);
            if (withDetail) {
                dto.children.addAll(searchChild(parentEntity.getParentId()));
            }

            dtoList.add(dto);
        });

		return dtoList;
    }
    
    @Transactional
	public List<NonExtendedChildTableDto> searchChild(Long parentId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QNonExtendedChildTableEntity.nonExtendedChildTableEntity.foreignkeyParent.parentId.eq(parentId));

        List<NonExtendedChildTableDto> dtoList = new ArrayList<>();

        List<NonExtendedChildTableEntity> resultList = queryFactory
                .select(QNonExtendedChildTableEntity.nonExtendedChildTableEntity)
                .from(QNonExtendedChildTableEntity.nonExtendedChildTableEntity)
                .where(builder)
                .fetch();

        resultList.stream().forEach(entity -> {
            NonExtendedChildTableDto dto = childConverter.fromEntity(entity);
            dtoList.add(dto);
        });

		return dtoList;
	}    
}
