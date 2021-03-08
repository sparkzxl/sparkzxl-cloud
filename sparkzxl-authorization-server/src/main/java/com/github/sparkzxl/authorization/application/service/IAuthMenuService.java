package com.github.sparkzxl.authorization.application.service;


import com.github.sparkzxl.authorization.domain.model.aggregates.MenuBasicInfo;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthMenu;
import com.github.sparkzxl.authorization.interfaces.dto.menu.AuthMenuSaveDTO;
import com.github.sparkzxl.database.base.service.SuperCacheService;

import java.util.List;

/**
 * description: 菜单 服务类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:31:12
 */
public interface IAuthMenuService extends SuperCacheService<AuthMenu> {

    /**
     * 查询菜单tree
     *
     * @return List<AuthMenu>
     */
    List<AuthMenu> findMenuTree();

    /**
     * 根据菜单id删除菜单
     *
     * @param ids 菜单ids
     * @return boolean
     */
    boolean deleteMenu(List<Long> ids);

    /**
     * 查询用户路由菜单
     *
     * @return List<MenuBasicInfo>
     */
    List<MenuBasicInfo> routers();

    /**
     * 保存菜单信息
     *
     * @param authMenuSaveDTO 菜单保存信息
     * @return boolean
     */
    boolean saveMenu(AuthMenuSaveDTO authMenuSaveDTO);
}
