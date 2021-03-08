package com.github.sparkzxl.authorization.domain.repository;

import java.math.BigDecimal;

/**
 * description: 序列生成仓储类
 *
 * @author: zhouxinlei
 * @date: 2021-02-20 17:38:32
 */
public interface IIdSegmentRepository {

    /**
     * 生成id
     *
     * @param businessTag 业务标记
     * @return BigDecimal
     */
    BigDecimal getIdSegment(String businessTag);

}
