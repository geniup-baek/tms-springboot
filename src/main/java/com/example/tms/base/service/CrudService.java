package com.example.tms.base.service;

import java.util.List;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;

import com.example.tms.base.BaseSearchCondition;
import com.example.tms.base.BaseService;
import com.example.tms.base.converter.EntityDtoConverter;
import com.example.tms.base.dto.CrudDto;
import com.example.tms.base.entity.ManagedEntity;
import com.example.tms.define.Const;
import com.example.tms.error.ApplicationException;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CrudService<E extends ManagedEntity<ID>, D extends CrudDto<ID>, R extends CrudRepository<E, ID>, V extends EntityDtoConverter<E, D, ID>, C extends BaseSearchCondition<ID>, ID> extends BaseService {

    C getIdSearchCondition(ID id);

    R getRepository();

    V getConverter();

    @Transactional
    default D read(ID id) {
        C searchCondition = getIdSearchCondition(id);

        List<D> resultList = search(searchCondition, Integer.MAX_VALUE, true);
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }

    @Transactional
    List<D> search(C searchCondition, int size, boolean withDetail);
    
    @Transactional
    default ID create(D dto) {
        E entity = getConverter().fromDtoForCreate(dto);
        E createdEntity = getRepository().save(entity);

        return createdEntity.getId();
    }
    
    @Transactional
    default D update(ID id, D dto) {

        D updatedDto = null;

        Optional<E> optionalEntity = getRepository().findById(id);
        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            if (!entity.getVersion().equals(dto.getVersion())) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }

            getConverter().mergeEntity(entity, getConverter().fromDto(dto));

            E updatedEntity = getRepository().save(entity);
            updatedDto = getConverter().fromEntity(updatedEntity);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }

        return updatedDto;
    }
    
    @Transactional
    default void delete(ID id, Integer version) {
        Optional<E> optionalEntity = getRepository().findById(id);

        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            getRepository().deleteById(id);
        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }
    
    @Transactional
    default void deleteLogical(ID id, Integer version) {

        Optional<E> optionalEntity = getRepository().findById(id);
        if (optionalEntity.isPresent()) {
            E entity = optionalEntity.get();
            if (!entity.getVersion().equals(version)) {
                throw new ApplicationException(Const.Message.OPTIMISTIC_LOCK_EXCEPTION);
            }
            entity.setDeleted(true);
            getRepository().save(entity);

        } else {
            throw new ApplicationException(Const.Message.NOT_FOUND);
        }
    }
    
    @Transactional
    default void deleteList(List<SimpleEntry<ID, Integer>> entryList) {
        for (SimpleEntry<ID, Integer> entry : entryList) {
            delete(entry.getKey(), entry.getValue());
        }
    }
    
    @Transactional
    default void deleteListLogical(List<SimpleEntry<ID, Integer>> entryList){
        for (SimpleEntry<ID, Integer> entry : entryList) {
            deleteLogical(entry.getKey(), entry.getValue());
        }
    }
}
