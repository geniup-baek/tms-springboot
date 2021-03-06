package com.example.tms.sample.familytable;

import java.util.ArrayList;
import java.util.List;

import com.example.tms.base.dto.CrudDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentTableDto implements CrudDto<Long> {

    private Long parentId;
    private String parent;
    private Integer version;
    private Boolean deleted;

    @Builder.Default()
    List<ChildTableDto> children = new ArrayList<>();

    @Override
    public Long getId() {
        return parentId;
    }    
}
