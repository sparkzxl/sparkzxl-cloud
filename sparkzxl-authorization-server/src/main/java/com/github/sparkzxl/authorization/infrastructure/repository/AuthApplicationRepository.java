package com.github.sparkzxl.authorization.infrastructure.repository;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.sparkzxl.authorization.domain.repository.IAuthApplicationRepository;
import com.github.sparkzxl.authorization.domain.repository.IDictionaryItemRepository;
import com.github.sparkzxl.authorization.domain.repository.IOauthClientDetailsRepository;
import com.github.sparkzxl.authorization.infrastructure.entity.AuthApplication;
import com.github.sparkzxl.authorization.infrastructure.entity.CommonDictionaryItem;
import com.github.sparkzxl.authorization.infrastructure.entity.OauthClientDetails;
import com.github.sparkzxl.authorization.infrastructure.mapper.AuthApplicationMapper;
import com.github.sparkzxl.core.context.BaseContextHandler;
import com.github.sparkzxl.core.support.SparkZxlExceptionAssert;
import com.github.sparkzxl.database.utils.PageInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * description: 应用 仓储实现类
 *
 * @author: zhouxinlei
 * @date: 2021-03-06 19:23:56
 */
@Repository
public class AuthApplicationRepository implements IAuthApplicationRepository {

    private final String SERVER_TYPE = "SERVER";
    @Autowired
    private AuthApplicationMapper authApplicationMapper;
    @Autowired
    private IOauthClientDetailsRepository oauthClientDetailsRepository;
    @Autowired
    private IDictionaryItemRepository dictionaryItemRepository;

    @Override
    public boolean saveAuthApplication(AuthApplication application) {
        if (application.getAppType().equals(SERVER_TYPE)) {
            OauthClientDetails oauthClientDetails = application.getOauthClientDetail();
            application.setOriginalClientSecret(oauthClientDetails.getClientSecret());
            oauthClientDetailsRepository.saveOauthClientDetails(oauthClientDetails);
        }
        authApplicationMapper.insert(application);
        return true;
    }

    @Override
    public PageInfo<AuthApplication> listPage(int pageNum, int pageSize, String clientId, String appName) {
        String tenantCode = BaseContextHandler.getTenant();
        if (StringUtils.isEmpty(tenantCode)) {
            SparkZxlExceptionAssert.businessFail("租户信息为空");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AuthApplication> authApplications = authApplicationMapper.listPage(tenantCode, clientId, appName);
        PageInfo<AuthApplication> authApplicationPageInfo = PageInfoUtils.pageInfo(authApplications);
        List<AuthApplication> applicationList = authApplicationPageInfo.getList();
        if (CollectionUtils.isNotEmpty(applicationList)) {
            List<String> clientIds = applicationList.stream().map(AuthApplication::getClientId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            Set<String> appTypeCodes = applicationList.stream().map(AuthApplication::getAppType).filter(ObjectUtils::isNotEmpty).collect(Collectors.toSet());
            Map<String, CommonDictionaryItem> dictionaryItemMap = dictionaryItemRepository.findDictionaryItemList("APPLICATION_TYPE",
                    appTypeCodes);
            List<OauthClientDetails> oauthClientDetails = oauthClientDetailsRepository.findListByIdList(clientIds);
            Map<String, OauthClientDetails> oauthClientDetailsMap = oauthClientDetails.stream().collect(Collectors.toMap(OauthClientDetails::getClientId, key -> key));
            applicationList.forEach(application -> {
                if (StringUtils.isNotEmpty(application.getClientId())) {
                    application.setOauthClientDetail(oauthClientDetailsMap.get(application.getClientId()));
                }
                CommonDictionaryItem commonDictionaryItem = dictionaryItemMap.get(application.getAppType());
                if (ObjectUtils.isNotEmpty(commonDictionaryItem)) {
                    application.setAppTypeName(commonDictionaryItem.getName());
                }
            });
            authApplicationPageInfo.setList(applicationList);
        }
        return authApplicationPageInfo;
    }

    @Override
    public boolean deleteAuthApplication(List<Long> ids) {
        List<AuthApplication> authApplications = authApplicationMapper.selectBatchIds(ids);
        List<String> clientIds = authApplications.stream().map(AuthApplication::getClientId).collect(Collectors.toList());
        oauthClientDetailsRepository.deleteClient(clientIds);
        return true;
    }

    @Override
    public boolean updateAuthApplication(AuthApplication application) {
        if (application.getAppType().equals(SERVER_TYPE)) {
            OauthClientDetails oauthClientDetails = application.getOauthClientDetail();
            application.setOriginalClientSecret(oauthClientDetails.getClientSecret());
            oauthClientDetailsRepository.updateOauthClientDetails(oauthClientDetails);
        }
        authApplicationMapper.updateById(application);
        return true;
    }
}
