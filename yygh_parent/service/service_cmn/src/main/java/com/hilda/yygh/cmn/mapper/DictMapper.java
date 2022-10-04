package com.hilda.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hilda.yygh.model.cmn.Dict;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictMapper extends BaseMapper<Dict> {

    List<Dict> getChildList(Long id);

}
