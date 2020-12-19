package com.example.tms.base.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.example.tms.base.BaseController;
import com.example.tms.base.BaseSearchCondition;
import com.example.tms.base.dto.FileIoDto;
import com.example.tms.base.service.FileIoService;
import com.example.tms.define.Const;
import com.example.tms.error.SystemException;
import com.example.tms.utility.ConvertUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface FileIoController<S extends FileIoService<D, C, ID>, D extends FileIoDto<ID>, C extends BaseSearchCondition<ID>, ID>
        extends BaseController {

    FileIoService<D, C, ID> getFileIoService();

    Class<C> getSearchControllerType();

    @GetMapping("/download")
    default void download(HttpServletResponse response,
            @RequestParam(value = "filename", required = true) String filename,
            @RequestParam(required = false) MultiValueMap<String, String> multiValueMap,
            @RequestParam(defaultValue = Const.SystemConfig.DOWNLOAD_MAX_SIZE_STRING) int size) {

        C searchCondition = ConvertUtils.fromMultiValueMap(multiValueMap, getSearchControllerType());

        String csv = getFileIoService().getCsv(searchCondition, size);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write(csv);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        }
    }
}
