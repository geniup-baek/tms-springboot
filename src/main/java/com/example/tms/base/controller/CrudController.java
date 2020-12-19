package com.example.tms.base.controller;

import java.net.URI;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.example.tms.base.BaseController;
import com.example.tms.base.BaseSearchCondition;
import com.example.tms.base.converter.EntityDtoConverter;
import com.example.tms.base.dto.CrudDto;
import com.example.tms.base.entity.ManagedEntity;
import com.example.tms.base.service.CrudService;
import com.example.tms.define.Const;
import com.example.tms.utility.ConvertUtils;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public interface CrudController<E extends ManagedEntity<ID>, D extends CrudDto<ID>, R extends CrudRepository<E, ID>, V extends EntityDtoConverter<E, D, ID>, C extends BaseSearchCondition<ID>, ID> extends BaseController {

    CrudService<E, D, R, V, C, ID> getCrudService();

    Class<C> getSearchControllerType();

    @GetMapping()
    default ResponseEntity<List<D>> search(
            @RequestParam(required = false) MultiValueMap<String, String> multiValueMap, 
            @RequestParam(defaultValue = Const.SystemConfig.SEARCH_MAX_SIZE_STRING) int size) {
                
        C searchCondition = ConvertUtils.fromMultiValueMap(multiValueMap, getSearchControllerType());

        List<D> dtoList = getCrudService().search(searchCondition, size, false);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    default ResponseEntity<D> read(HttpServletRequest request, @PathVariable("id") ID id) {

        D dto = getCrudService().read(id);

        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    default ResponseEntity<Void> create(HttpServletRequest request, @RequestBody D dto) {
        ID id = getCrudService().create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    default ResponseEntity<D> update(HttpServletRequest request, @PathVariable("id") ID id, @RequestBody D dto) {
        D updatedDto = getCrudService().update(id, dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}:{version}")
    default ResponseEntity<HttpStatus> delete(HttpServletRequest request, @PathVariable("id") ID id, @PathVariable("version") Integer version) {
        getCrudService().delete(id, version);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/list")
    default ResponseEntity<HttpStatus> deleteList(HttpServletRequest request, @RequestBody List<D> dtoList) {
        List<SimpleEntry<ID, Integer>> idVersionList = dtoList.stream()
                .map(dto -> new SimpleEntry<ID, Integer>(dto.getId(), dto.getVersion())).collect(Collectors.toList());
        getCrudService().deleteList(idVersionList);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/delete/{id}:{version}")
    default ResponseEntity<HttpStatus> deleteLogical(HttpServletRequest request, @PathVariable("id") ID id, @PathVariable("version") Integer version) {
        getCrudService().deleteLogical(id, version);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/delete-list")
    default ResponseEntity<HttpStatus> deleteLogicalList(HttpServletRequest request, @RequestBody List<D> dtoList) {
        List<SimpleEntry<ID, Integer>> idVersionList = dtoList.stream()
                .map(dto -> new SimpleEntry<ID, Integer>(dto.getId(), dto.getVersion())).collect(Collectors.toList());
        getCrudService().deleteListLogical(idVersionList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
