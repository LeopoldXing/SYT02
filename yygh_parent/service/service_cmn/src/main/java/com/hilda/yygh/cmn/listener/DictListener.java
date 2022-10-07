package com.hilda.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hilda.yygh.cmn.mapper.DictMapper;
import com.hilda.yygh.model.cmn.Dict;
import com.hilda.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//EasyExcel官网不建议将该监听器放入Spring容器中
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private static final int BATCH_COUNT = 500;
    List<Dict> dictList = new ArrayList<>();

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //每次解析excel文件中的一行数据，都会调用一次invoke()方法
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);

        dictMapper.insert(dict);
        /*dictList.add(dict);
        if(dictList.size() >= BATCH_COUNT) {
            dictMapper.insertBatchSomeColumn(dictList);
        }*/
    }

    //当开始解析excel文件中的某个sheet的时候调用
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
    }

    //当excel文件被解析完毕之后，会调用该方法，批量处理时可能会用到
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        dictMapper.insertBatchSomeColumn(dictList);
    }
}
