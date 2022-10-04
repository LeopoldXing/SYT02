package com.hilda.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.yygh.model.cmn.Dict;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {

    List<Dict> getChildList(Long id);

    void exportDictData(HttpServletResponse response);

}
