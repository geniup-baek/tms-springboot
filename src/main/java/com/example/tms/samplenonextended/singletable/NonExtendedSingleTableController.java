package com.example.tms.samplenonextended.singletable;

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
@RequestMapping("/api/non-extended/sample-singles")
public class NonExtendedSingleTableController {
    
    private NonExtendedSingleTableService service;

    @Autowired
    public NonExtendedSingleTableController(NonExtendedSingleTableService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Void> create(HttpServletRequest request, @RequestBody NonExtendedSingleTableDto dto) {
        Long id = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NonExtendedSingleTableDto> update(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody NonExtendedSingleTableDto dto) {
        NonExtendedSingleTableDto updatedDto = service.update(id, dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}:{version}")
    public ResponseEntity<HttpStatus> delete(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("version") Integer version) {
        service.delete(id, version);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/list")
    public ResponseEntity<HttpStatus> deleteList(HttpServletRequest request, @RequestBody List<NonExtendedSingleTableDto> dtoList) {
        List<SimpleEntry<Long, Integer>> idVersionList = dtoList.stream()
                .map(dto -> new SimpleEntry<Long, Integer>(dto.getSingleId(), dto.getVersion()))
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
    public ResponseEntity<HttpStatus> deleteLogicalList(HttpServletRequest request, @RequestBody List<NonExtendedSingleTableDto> dtoList) {
        List<SimpleEntry<Long, Integer>> idVersionList = dtoList.stream()
                .map(dto -> new SimpleEntry<Long, Integer>(dto.getSingleId(), dto.getVersion()))
                .collect(Collectors.toList());
        service.deleteListLogical(idVersionList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }    

    @GetMapping()
    public ResponseEntity<List<NonExtendedSingleTableDto>> search(
            @RequestParam(required = false) MultiValueMap<String, String> multiValueMap,
            @RequestParam(defaultValue = Const.SystemConfig.SEARCH_MAX_SIZE_STRING) int size) {
        
        NonExtendedSingleTableSearchCondition searchCondition = ConvertUtils.fromMultiValueMap(multiValueMap, NonExtendedSingleTableSearchCondition.class);

        List<NonExtendedSingleTableDto> dtoList = service.search(searchCondition, size);

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NonExtendedSingleTableDto> read(HttpServletRequest request, @PathVariable("id") Long id) {
        
        NonExtendedSingleTableDto dto = service.read(id);

        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
