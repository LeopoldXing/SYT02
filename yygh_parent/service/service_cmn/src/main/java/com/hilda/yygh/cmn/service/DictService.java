package com.hilda.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.yygh.model.cmn.Dict;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface DictService extends IService<Dict> {

    List<Dict> getChildList(Long id);

    void exportDictData(OutputStream outputStream, String fileName);

    void importDictData(InputStream inputStream);

    String getNameByParentDictCodeAndValue(String parentDictCode, Long value);

    String getNameByValue(Long value);
}
