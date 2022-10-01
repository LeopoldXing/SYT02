package com.hilda.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.yygh.cmn.mapper.DictMapper;
import com.hilda.yygh.cmn.service.DictService;
import com.hilda.yygh.model.cmn.Dict;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
}
