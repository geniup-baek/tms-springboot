package com.example.tms.sample.familytable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample-families")
public class FamilyController {

    ParentTableRepository parentRepository;
    ChildTableRepository childRepository;

    @Autowired
    public FamilyController(ParentTableRepository parentRepository, ChildTableRepository childRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }


    @GetMapping("/parents")
    public ResponseEntity<List<ParentTableEntity>> fineAllParents() {
        
        List<ParentTableEntity> resultList = new ArrayList<>();

        parentRepository.findAll().forEach(resultList::add);

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/children")
    public ResponseEntity<List<ChildTableEntity>> fineAllChildren() {
        
        List<ChildTableEntity> resultList = new ArrayList<>();

        childRepository.findAll().forEach(resultList::add);

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<FamilyDto>> fineAllFamilies() {
        
        List<FamilyDto> resultList = new ArrayList<>();

        parentRepository.findAll().forEach(parent -> {

            FamilyDto dto = FamilyDto.builder()
                    .id(parent.getId())
                    .parent(parent.getParentField())
                    .version(parent.getVersion())
                    .deleted(parent.getDeleted())
                    .build();
            childRepository.findAllByForeignkeyParentId(parent.getId()).forEach(dto.children::add);
            resultList.add(dto);
        });
        
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

}
