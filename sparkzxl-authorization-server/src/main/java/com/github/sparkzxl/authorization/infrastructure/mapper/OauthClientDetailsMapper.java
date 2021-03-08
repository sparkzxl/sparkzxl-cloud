package com.github.sparkzxl.authorization.infrastructure.mapper;

import com.github.sparkzxl.authorization.infrastructure.entity.OauthClientDetails;
import com.github.sparkzxl.database.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: 应用客户端 Mapper 接口
 *
 * @author: zhouxinlei
 * @date: 2021-02-02 11:34:50
 */
@Repository
public interface OauthClientDetailsMapper extends SuperMapper<OauthClientDetails> {


    /**
     * 获取客户端分页信息
     *
     * @param tenantCode 租户code
     * @param clientId   客户端id
     * @return List<OauthClientDetails>
     */
    @Select("<script> " +
            "SELECT ocd.*," +
            "tc.id, " +
            "tc.tenant_code tenantCode, " +
            "tc.original_client_secret originalClientSecret," +
            "ti.name tenantName " +
            "FROM oauth_client_details ocd " +
            "INNER JOIN tenant_client tc ON ocd.client_id = tc.client_id " +
            "INNER JOIN tenant_info ti ON ti.code = tc.tenant_code " +
            "<where>" +
            " <if test=\"tenantCode != null and tenantCode != ''\">" +
            "    and tc.tenant_code = #{tenantCode}" +
            " </if>" +
            " <if test=\"clientId != null and clientId != ''\">" +
            "    and ocd.client_id = #{clientId}" +
            " </if>" +
            "</where>" +
            "</script>")
    List<OauthClientDetails> listPage(@Param("tenantCode") String tenantCode, @Param("clientId") String clientId);

}
