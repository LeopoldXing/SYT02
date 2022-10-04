package com.hilda.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.yygh.cmn.mapper.DictMapper;
import com.hilda.yygh.cmn.service.DictService;
import com.hilda.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<Dict> getChildList(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = dictMapper.selectList(queryWrapper);
        //设置列表中数据字典的hasChildren属性
        dictList.forEach(dict -> dict.setHasChildren(hasChild(dict.getId())));

        return dictList;
    }

    @Override
    public void exportDictData(HttpServletResponse response) {

    }

    private Boolean hasChild(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);

        Integer count = dictMapper.selectCount(queryWrapper);
        return count > 0;
    }

}
