package com.github.sparkzxl.authorization.infrastructure.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.sparkzxl.authorization.application.event.RoleResourceEvent;
import com.github.sparkzxl.authorization.domain.model.aggregates.ResourceSource;
import com.github.sparkzxl.authorization.domain.repository.IAuthResourceRepository;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthResource;
import com.github.sparkzxl.authorization.infrastructure.entity.RoleAuthority;
import com.github.sparkzxl.authorization.infrastructure.enums.OperationEnum;
import com.github.sparkzxl.authorization.infrastructure.mapper.AuthResourceMapper;
import com.github.sparkzxl.authorization.infrastructure.mapper.RoleAuthorityMapper;
import com.github.sparkzxl.core.spring.SpringContextUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: 资源 仓储层实现类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:31:28
 */
@AllArgsConstructor
@Repository
public class AuthResourceRepository implements IAuthResourceRepository {

    private final AuthResourceMapper authResourceMapper;
    private final RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public List<AuthResource> authResourceList() {
        return authResourceMapper.selectList(null);
    }

    @Override
    public List<AuthResource> authResourceList(List<Long> menuIds) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            return authResourceMapper.selectList(new LambdaQueryWrapper<AuthResource>().in(AuthResource::getMenuId, menuIds));
        }
        return Lists.newArrayList();
    }

    @Override
    public List<AuthResource> findVisibleResource(Long userId, Long menuId) {
        return authResourceMapper.findVisibleResource(userId, menuId);
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        roleAuthorityMapper.delete(new LambdaQueryWrapper<RoleAuthority>().eq(RoleAuthority::getAuthorityId, resourceId));
        AuthResource authResource = authResourceMapper.selectById(resourceId);
        SpringContextUtils.publishEvent(new RoleResourceEvent(new ResourceSource(OperationEnum.DELETE, null, authResource.getRequestUrl())));
        return authResourceMapper.deleteById(resourceId) == 1;
    }

    @Override
    public void saveResourceList(List<AuthResource> resourceList) {
        authResourceMapper.insertBatchSomeColumn(resourceList);
    }

    @Override
    public void deleteTenantResource(String tenantCode) {
        authResourceMapper.deleteTenantResource(tenantCode);
    }

    @Override
    public boolean updateResource(AuthResource authResource) {
        if (StringUtils.isNotEmpty(authResource.getRequestUrl())) {
            Long resourceId = authResource.getId();
            AuthResource oldResource = authResourceMapper.selectById(resourceId);
            String oldRequestUrl = oldResource.getRequestUrl();
            authResourceMapper.updateById(authResource);
            SpringContextUtils.publishEvent(new RoleResourceEvent(new ResourceSource(OperationEnum.UPDATE,
                    authResource.getRequestUrl(), oldRequestUrl)));
        } else {
            authResourceMapper.updateById(authResource);
        }
        return true;
    }
}
