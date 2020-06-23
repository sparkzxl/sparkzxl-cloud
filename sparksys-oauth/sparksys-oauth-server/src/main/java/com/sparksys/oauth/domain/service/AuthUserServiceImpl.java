package com.sparksys.oauth.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sparksys.oauth.application.service.IAuthUserService;
import com.sparksys.oauth.domain.constant.AuthorizationConstant;
import com.sparksys.oauth.domain.repository.IAuthUserRepository;
import com.sparksys.oauth.infrastructure.convert.AuthUserConvert;
import com.sparksys.oauth.infrastructure.entity.AuthUser;
import com.sparksys.oauth.infrastructure.entity.AuthUserDetail;
import com.sparksys.oauth.interfaces.dto.user.AuthUserDTO;
import com.sparksys.oauth.interfaces.dto.user.AuthUserSaveDTO;
import com.sparksys.oauth.interfaces.dto.user.AuthUserStatusDTO;
import com.sparksys.oauth.interfaces.dto.user.AuthUserUpdateDTO;
import com.sparksys.commons.core.base.api.result.ApiPageResult;
import com.sparksys.commons.mybatis.page.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * description: 用户查询 服务实现类
 *
 * @author zhouxinlei
 * @date 2020-05-24 12:22:57
 */
@Service
@Slf4j
public class AuthUserServiceImpl implements IAuthUserService {

    private final IAuthUserRepository authUserRepository;

    public AuthUserServiceImpl(IAuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }


    @Override
    public boolean saveAuthUser(com.sparksys.commons.core.entity.AuthUser authUser, AuthUserSaveDTO authUserSaveDTO) {
        AuthUser authUserDO = AuthUserConvert.INSTANCE.convertAuthUserDO(authUserSaveDTO);
        return authUserRepository.saveAuthUser(authUserDO);
    }

    @Override
    public boolean updateAuthUser(com.sparksys.commons.core.entity.AuthUser authUser, AuthUserUpdateDTO authUserUpdateDTO) {
        AuthUser authUserDO = AuthUserConvert.INSTANCE.convertAuthUserDO(authUserUpdateDTO);
        return authUserRepository.updateAuthUser(authUserDO);
    }

    @Override
    public boolean deleteAuthUser(Long id) {
        return authUserRepository.deleteAuthUser(id);
    }

    @Override
    public boolean updateAuthUserStatus(com.sparksys.commons.core.entity.AuthUser authUser, AuthUserStatusDTO authUserStatusDTO) {
        authUserStatusDTO.setUpdateUser(authUser.getId());
        AuthUser authUserDO = AuthUserConvert.INSTANCE.convertAuthUserDO(authUserStatusDTO);
        return authUserRepository.updateAuthUser(authUserDO);
    }

    @Override
    public ApiPageResult listByPage(Integer pageNum, Integer pageSize, String name) {
        Page<AuthUser> userDOIPage = authUserRepository.listByPage(new Page(pageNum, pageSize), name);
        List<AuthUser> authUserList = userDOIPage.getRecords();
        List<AuthUserDTO> authUserDTOS =
                authUserList.stream().map(authUserDO -> {
                    AuthUserDTO authUserDTO = AuthUserConvert.INSTANCE.convertAuthUserDTO(authUserDO);
                    String sex = AuthorizationConstant.SEX_MAP.get(authUserDO.getSex());
                    authUserDTO.setSex(sex);
                    return authUserDTO;
                }).collect(Collectors.toList());
        return PageResult.resetPage(userDOIPage, authUserDTOS);
    }

    @Override
    public AuthUserDTO getAuthUser(Long id) {
        AuthUser authUser = authUserRepository.selectById(id);
        AuthUserDTO authUserDTO = AuthUserConvert.INSTANCE.convertAuthUserDTO(authUser);
        String sex = AuthorizationConstant.SEX_MAP.get(authUser.getSex());
        authUserDTO.setSex(sex);
        return authUserDTO;
    }

    @Override
    public boolean resetPassErrorNum(Long id) {
        AuthUser authUser = new AuthUser();
        authUser.setId(id);
        authUser.setPasswordErrorNum(0);
        authUser.setPasswordErrorLastTime(null);
        return authUserRepository.updateAuthUser(authUser);
    }

    @Override
    public boolean incrPasswordErrorNumById(Long id) {
        return authUserRepository.incrPasswordErrorNumById(id);
    }

    @Override
    public AuthUserDetail getAuthUserDetail(String username) {
        AuthUser authUser = authUserRepository.selectByAccount(username);
        if (ObjectUtils.isNotEmpty(authUser)) {
            return new AuthUserDetail(authUser.getAccount(), authUser.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        }
        return null;
    }
}