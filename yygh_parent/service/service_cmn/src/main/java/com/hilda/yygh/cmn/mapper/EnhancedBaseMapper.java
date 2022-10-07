package com.hilda.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.collection.CollUtil;

import java.util.List;

public interface EnhancedBaseMapper<T> extends BaseMapper<T> {

    /**
     * 默认批次提交数量
     */
    int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 批量新增数据,自选字段 insert. 自动按每批1000插入数据库
     * * 此填充不会填充 FieldFill.UPDATE 的字段。
     * * 注意数据库默认更新的字段也需要手工设置
     *
     * @param entityList
     * @return 插入的条数
     */
    @Transactional(rollbackFor = Exception.class)
    default Integer insertBatch(List<T> entityList) {
        return this.insertBatchSomeColumn(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量新增数据,自选字段 insert
     * 不会分批插入，需要分批请调用方法insertBatch或者 insertBatchSomeColumn(List<T> entityList, int size)
     * 此填充不会填充 FieldFill.UPDATE 的字段。
     * 注意数据库默认更新的字段也需要手工设置
     *
     * @param entityList 数据
     * @return 插入条数
     */
    int insertBatchSomeColumn(List<T> entityList);

    /**
     * 分批插入。每次插入
     *
     * @param entityList 原实体对象
     * @param size       分批大小
     * @return 总插入记录
     */
    @Transactional(rollbackFor = Exception.class)
    default int insertBatchSomeColumn(List<T> entityList, int size) {
        if (entityList == null || entityList.size() == 0) {
            return 0;
        }
        List<List<T>> split = CollUtil.split(entityList, size);
        return split.stream().mapToInt(this::insertBatchSomeColumn).sum();
    }

}
