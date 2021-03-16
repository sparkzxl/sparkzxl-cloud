package com.github.sparkzxl.authorization.interfaces.controller.auth;

import com.github.sparkzxl.authorization.application.service.IAuthRoleService;
import com.github.sparkzxl.authorization.application.service.IUserRoleService;
import com.github.sparkzxl.authorization.domain.model.vo.RoleResourceVO;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthRole;
import com.github.sparkzxl.authorization.interfaces.dto.role.*;
import com.github.sparkzxl.core.annotation.ResponseResult;
import com.github.sparkzxl.core.entity.AuthUserInfo;
import com.github.sparkzxl.database.base.controller.SuperCacheController;
import com.github.sparkzxl.log.annotation.WebLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * description: 角色 前端控制器
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:40:03
 */
@RestController
@RequestMapping("/role")
@ResponseResult
@WebLog
@Api(tags = "角色管理")
public class AuthRoleController extends SuperCacheController<IAuthRoleService, Long,
        AuthRole, RoleSaveDTO, RoleUpdateDTO, RoleQueryDTO, Object> {

    private IUserRoleService userRoleService;

    @Autowired
    public void setUserRoleService(IUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public boolean handlerDelete(List<Long> ids) {
        baseService.deleteAuthRoleRelation(ids);
        return true;
    }

    @ApiOperation("更新角色状态")
    @PatchMapping("/role/{id}")
    public boolean updateAuthRoleStatus(@ApiIgnore AuthUserInfo<Long> authUserInfo, @PathVariable("id") Long id,
                                        @RequestParam(value = "status") Boolean status) {
        return baseService.updateAuthRoleStatus(authUserInfo.getId(), id, status);
    }

    @ApiOperation("保存角色用户")
    @PostMapping("/user/save")
    public boolean saveAuthRoleUser(@Validated @RequestBody RoleUserSaveDTO roleUserSaveDTO) {
        return userRoleService.saveAuthRoleUser(roleUserSaveDTO);
    }


    @ApiOperation("删除角色用户")
    @DeleteMapping("/user/delete")
    public boolean deleteAuthRoleUser(@Validated @RequestBody RoleUserDeleteDTO roleUserDeleteDTO) {
        return userRoleService.deleteAuthRoleUser(roleUserDeleteDTO);
    }

    @ApiOperation("查询角色用户")
    @GetMapping("/user/list")
    public RoleUserDTO getRoleUserList(@RequestParam("roleId") Long roleId) {
        return userRoleService.getRoleUserList(roleId);
    }

    @ApiOperation("查询角色资源")
    @GetMapping("/resource")
    public RoleResourceVO getRoleResource(@RequestParam("roleId") Long roleId) {
        return userRoleService.getRoleResource(roleId);
    }

}
