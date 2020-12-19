package com.example.tms.sample.familytable;

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
public class FamilyService {

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

    @Transactional
    public ParentTableDto read(Long id) {
        FamilySearchCondition searchCondition = FamilySearchCondition.builder().build();
        searchCondition.setId(id);

        List<ParentTableDto> resultList = search(searchCondition, Integer.MAX_VALUE, true);
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }

    @Transactional
    public Long create(ParentTableDto dto) {

        ParentTableEntity parentEntity = parentConverter.fromDtoForCreate(dto);
        ParentTableEntity createdParentEntity = parentRepository.save(parentEntity);

        dto.children.stream().forEach(childDto -> {

            ChildTableEntity childEntity = childConverter.fromDtoForCreate(childDto);
            childEntity.setForeignkeyParent(createdParentEntity);

            childRepository.save(childEntity);
        });

        return createdParentEntity.getId();
    }

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
                        Optional<ChildTableEntity> optionalChildEntity = childRepository.findById(childDto.getId());
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
                        Optional<ChildTableEntity> optionalChildEntity = childRepository.findById(childDto.getId());
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

            updatedDto = read(updatedParentEntity.getId()); // parentConverter.fromEntity(updatedParentEntity);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }

        return updatedDto;
    }

    @Transactional
    public void delete(Long id, Integer version) {
        Optional<ParentTableEntity> optionalEntity = parentRepository.findById(id);

        if (optionalEntity.isPresent()) {
            ParentTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            // delete children
            childRepository.deleteByForeignkeyParentId(id);

            parentRepository.deleteById(id);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }

    @Transactional
    public void deleteLogical(Long id, Integer version) {

        Optional<ParentTableEntity> optionalEntity = parentRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ParentTableEntity entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }

            // delete children
            childRepository.findAllByForeignkeyParentId(id).forEach(childEntity -> {
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
	public List<ParentTableDto> search(FamilySearchCondition searchCondition, int size, boolean withDetail) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchCondition.getId() != null) {
            builder.and(QParentTableEntity.parentTableEntity.id.eq(searchCondition.getId()));
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
                dto.children.addAll(searchChild(parentEntity.getId()));
            }

            dtoList.add(dto);
        });

		return dtoList;
    }
    
    @Transactional
	public List<ChildTableDto> searchChild(Long parentId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QChildTableEntity.childTableEntity.foreignkeyParent.id.eq(parentId));

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
