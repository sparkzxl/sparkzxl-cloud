package com.github.sparkzxl.authorization.infrastructure.mapper;

import com.github.sparkzxl.authorization.infrastructure.entity.TenantInfo;
import com.github.sparkzxl.database.base.mapper.SuperMapper;
import org.springframework.stereotype.Repository;

/**
 * description: 领域池 Mapper 接口
 *
 * @author charles.zhou
 * @date   2021-02-02 16:09:50
 */
@Repository
public interface TenantInfoMapper extends SuperMapper<TenantInfo> {

}
