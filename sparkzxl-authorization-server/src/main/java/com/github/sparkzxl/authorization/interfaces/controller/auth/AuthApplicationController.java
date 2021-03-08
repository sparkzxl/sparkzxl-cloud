package com.github.sparkzxl.authorization.interfaces.controller.auth;


import com.github.pagehelper.PageInfo;
import com.github.sparkzxl.authorization.application.service.IAuthApplicationService;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthApplication;
import com.github.sparkzxl.authorization.infrastructure.entity.OauthClientDetails;
import com.github.sparkzxl.authorization.interfaces.dto.application.AuthApplicationPageDTO;
import com.github.sparkzxl.authorization.interfaces.dto.application.AuthApplicationSaveDTO;
import com.github.sparkzxl.authorization.interfaces.dto.application.AuthApplicationUpdateDTO;
import com.github.sparkzxl.database.dto.DeleteDTO;
import com.github.sparkzxl.core.annotation.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * description: 应用客户端管理
 *
 * @author: zhouxinlei
 * @date: 2021-02-02 11:31:18
 */
@RestController
@Api(tags = "应用客户端管理")
@ResponseResult
@RequestMapping("/application")
public class AuthApplicationController {

    @Autowired
    private IAuthApplicationService authApplicationService;

    @ApiOperation("保存应用信息")
    @PostMapping("/saveApplication")
    public boolean saveApplication(@Validated @RequestBody AuthApplicationSaveDTO authApplicationSaveDTO) {
        return authApplicationService.saveApplication(authApplicationSaveDTO);
    }

    @ApiOperation("更新应用信息")
    @PutMapping("/updateApplication")
    public boolean updateApplication(@Validated @RequestBody AuthApplicationUpdateDTO authApplicationUpdateDTO) {
        return authApplicationService.updateApplication(authApplicationUpdateDTO);
    }

    @ApiOperation("删除应用信息")
    @DeleteMapping("/deleteApplication")
    public boolean deleteClient(@RequestBody DeleteDTO deleteDTO) {
        return authApplicationService.deleteApplication(deleteDTO);
    }

    @ApiOperation("获取应用分页")
    @PostMapping("/listPage")
    public PageInfo<AuthApplication> listPage(@RequestBody AuthApplicationPageDTO authApplicationPageDTO) {
        return authApplicationService.listPage(authApplicationPageDTO);
    }
}
