package com.example.tms.samplenonextended.familytable;

import java.net.URI;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.example.tms.define.Const;
import com.example.tms.utility.ConvertUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/non-extended/sample-families")
public class NonExtendedFamilyController {

    private NonExtendedFamilyService service;

    @Autowired
    public NonExtendedFamilyController(NonExtendedFamilyService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Void> create(HttpServletRequest request, @RequestBody NonExtendedParentTableDto dto) {
        Long id = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NonExtendedParentTableDto> update(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody NonExtendedParentTableDto dto) {
        NonExtendedParentTableDto updatedDto = service.update(id, dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}:{version}")
    public ResponseEntity<HttpStatus> delete(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("version") Integer version) {
        service.delete(id, version);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/list")
    public ResponseEntity<HttpStatus> deleteList(HttpServletRequest request, @RequestBody List<NonExtendedParentTableDto> dtoList) {
        List<SimpleEntry<Long, Integer>> idVersionList = dtoList.stream()
                .map(dto -> new SimpleEntry<Long, Integer>(dto.getParentId(), dto.getVersion()))
                .collect(Collectors.toList());
        service.deleteList(idVersionList);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/delete/{id}:{version}")
    public ResponseEntity<HttpStatus> deleteLogical(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("version") Integer version) {
        service.deleteLogical(id, version);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }    

    @PutMapping("/delete-list")
    public ResponseEntity<HttpStatus> deleteLogicalList(HttpServletRequest request, @RequestBody List<NonExtendedParentTableDto> dtoList) {
        List<SimpleEntry<Long, Integer>> idVersionList = dtoList.stream()
                .map(dto -> new SimpleEntry<Long, Integer>(dto.getParentId(), dto.getVersion()))
                .collect(Collectors.toList());
        service.deleteListLogical(idVersionList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }    

    @GetMapping()
    public ResponseEntity<List<NonExtendedParentTableDto>> search(
            @RequestParam(required = false) MultiValueMap<String, String> multiValueMap,
            @RequestParam(defaultValue = Const.SystemConfig.SEARCH_MAX_SIZE_STRING) int size) {
        
        NonExtendedFamilySearchCondition searchCondition = ConvertUtils.fromMultiValueMap(multiValueMap, NonExtendedFamilySearchCondition.class);

        List<NonExtendedParentTableDto> dtoList = service.search(searchCondition, size, false);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NonExtendedParentTableDto> read(HttpServletRequest request, @PathVariable("id") Long id) {
        
        NonExtendedParentTableDto dto = service.read(id);

        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    
}
