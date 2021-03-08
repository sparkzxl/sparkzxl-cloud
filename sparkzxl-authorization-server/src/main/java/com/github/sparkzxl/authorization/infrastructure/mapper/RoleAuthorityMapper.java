package com.github.sparkzxl.authorization.infrastructure.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.github.sparkzxl.authorization.infrastructure.entity.RoleAuthority;
import com.github.sparkzxl.database.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

/**
 * description: 角色资源 Mapper 接口
 *
 * @author: zhouxinlei
 * @date: 2020-07-19 20:57:42
 */
@Repository
public interface RoleAuthorityMapper extends SuperMapper<RoleAuthority> {

    /**
     * 根据租户code删除角色资源
     * @param tenantCode 租户code
     */
    @Delete("delete from auth_role_authority where tenant_code = #{tenantCode}")
    @InterceptorIgnore(tenantLine = "true")
    void deleteRoleAuthority(String tenantCode);
}
