package com.hilda.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.yygh.cmn.listener.DictListener;
import com.hilda.yygh.cmn.mapper.DictMapper;
import com.hilda.yygh.cmn.service.DictService;
import com.hilda.yygh.model.cmn.Dict;
import com.hilda.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Cacheable(cacheNames = "dict", key = "'value_' + #id")
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
    public void exportDictData(OutputStream outputStream, String fileName) {
        //准备数据字典数据
        List<Dict> dictList = dictMapper.selectList(null);
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());

        //将dict列表 转换为 dictEeVo列表
        dictList.forEach(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        });

        //将数据写入Excel文件
        EasyExcel.write(outputStream, DictEeVo.class).sheet(fileName).doWrite(dictEeVoList);
    }

    @CacheEvict(value = "dict",allEntries = true)
    @Override
    public void importDictData(InputStream inputStream) {
        EasyExcel
                .read(inputStream, DictEeVo.class, new DictListener(dictMapper))
                .sheet(0)
                .doRead();
    }

    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, Long value) {
        Dict parentDict = dictMapper.selectOne(new QueryWrapper<Dict>().eq("dict_code", parentDictCode));
        Dict dict = dictMapper.selectOne(new QueryWrapper<Dict>().eq("value", value).eq("parent_id", parentDict.getId()));
        return dict.getName();
    }

    @Override
    public String getNameByValue(Long value) {
        return dictMapper.selectOne(new QueryWrapper<Dict>().eq("value", value)).getName();
    }

    @Override
    public List<Dict> getChildListByDictCode(String parentCode) {
        return dictMapper.selectList(new QueryWrapper<Dict>().eq("dict_code", parentCode));
    }

    private Boolean hasChild(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);

        Integer count = dictMapper.selectCount(queryWrapper);
        return count > 0;
    }

}
