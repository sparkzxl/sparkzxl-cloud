package com.github.sparkzxl.authorization.domain.repository;


import com.github.sparkzxl.authorization.infrastructure.entity.AuthResource;

import java.util.List;

/**
 * description: 资源 仓储类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:31:28
 */
public interface IAuthResourceRepository {

    /**
     * 加载所有资源
     *
     * @return List<AuthResource>
     */
    List<AuthResource> authResourceList();


    /**
     * 加载指定菜单所有资源
     *
     * @param menuIds 菜单id列表
     * @return List<AuthResource>
     */
    List<AuthResource> authResourceList(List<Long> menuIds);

    /**
     * 获取用户资源
     *
     * @param userId 用户id
     * @param menuId 菜单id
     * @return List<AuthResource>
     */
    List<AuthResource> findVisibleResource(Long userId, Long menuId);

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return boolean
     */
    boolean deleteResource(Long resourceId);

    /**
     * 保存资源信息列表
     *
     * @param resourceList 资源信息列表
     */
    void saveResourceList(List<AuthResource> resourceList);

    /**
     * 根据租户code删除资源
     *
     * @param tenantCode 租户code
     */
    void deleteTenantResource(String tenantCode);

    /**
     * 更新资源
     *
     * @param authResource 资源
     * @return boolean
     */
    boolean updateResource(AuthResource authResource);
}
