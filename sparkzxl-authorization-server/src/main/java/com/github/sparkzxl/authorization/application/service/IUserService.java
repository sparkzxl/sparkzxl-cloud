package com.github.sparkzxl.authorization.application.service;

import com.github.pagehelper.PageInfo;
import com.github.sparkzxl.authorization.domain.model.aggregates.MenuBasicInfo;
import com.github.sparkzxl.authorization.domain.model.vo.AuthUserBasicVO;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthUser;
import com.github.sparkzxl.authorization.interfaces.dto.user.UserQueryDTO;
import com.github.sparkzxl.authorization.interfaces.dto.user.UserSaveDTO;
import com.github.sparkzxl.authorization.interfaces.dto.user.UserUpdateDTO;
import com.github.sparkzxl.core.entity.AuthUserInfo;
import com.github.sparkzxl.database.base.service.SuperCacheService;
import com.github.sparkzxl.database.dto.PageParams;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * description: 用户查询 服务类
 *
 * @author charles.zhou
 * @date 2020-05-24 12:22:23
 */
public interface IUserService extends SuperCacheService<AuthUser> {

    /**
     * 获取全局用户信息
     *
     * @param username 用户账户
     * @return AuthUserInfo<Long>
     */
    AuthUserInfo<Long> getAuthUserInfo(String username);

    /**
     * 根据账户查询用户信息
     *
     * @param username 账户
     * @return AuthUser
     */
    AuthUser getByAccount(String username);

    /**
     * 获取用户分页
     *
     * @param params 分页入参
     * @return PageInfo<AuthUser>
     */
    PageInfo<AuthUser> getAuthUserPage(PageParams<UserQueryDTO> params);

    /**
     * 保存用户信息
     *
     * @param authUserSaveDTO AuthUserSaveDTO保存对象
     * @return boolean
     */
    boolean saveAuthUser(UserSaveDTO authUserSaveDTO);

    /**
     * 修改用户信息
     *
     * @param authUserUpdateDTO AuthUserSaveDTO修改对象
     * @return boolean
     */
    boolean updateAuthUser(UserUpdateDTO authUserUpdateDTO);

    /**
     * 批量更新用户组织
     *
     * @param orgIds 组织ids
     */
    void deleteOrgIds(List<Long> orgIds);

    /**
     * 生成仿真数据
     *
     * @return boolean
     */
    boolean mockUserData();

    /**
     * 获取登录用户全量信息
     *
     * @param userId 用户id
     * @return AuthUserBasicVO
     */
    AuthUserBasicVO getAuthUserBasicInfo(Long userId);

    /**
     * 用户菜单
     *
     * @param userId 用户id
     * @return List<MenuBasicInfo>
     */
    List<MenuBasicInfo> routers(Long userId);

    /**
     * Excel导入用户数据
     *
     * @param multipartFile 文件
     * @return Integer
     */
    Integer importUserData(MultipartFile multipartFile);

    /**
     * 查询用户列表
     *
     * @param userQueryDTO 用户查询参数
     * @return List<AuthUser>
     */
    List<AuthUser> userList(UserQueryDTO userQueryDTO);

    /**
     * 删除用户信息
     *
     * @param ids ids
     * @return boolean
     */
    boolean deleteAuthUser(List<Long> ids);
}
