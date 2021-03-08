package com.github.sparkzxl.authorization.infrastructure.config;

import com.github.sparkzxl.core.resource.SwaggerStaticResource;
import com.github.sparkzxl.core.utils.ListUtils;
import com.github.sparkzxl.open.component.RestAuthenticationEntryPoint;
import com.github.sparkzxl.open.component.RestfulAccessDeniedHandler;
import com.github.sparkzxl.open.properties.SecurityProperties;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.List;

/**
 * description: 资源服务器
 *
 * @author: zhouxinlei
 * @date: 2021-02-01 11:30:00
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        RestAuthenticationEntryPoint restAuthenticationEntryPoint = new RestAuthenticationEntryPoint();
        RestfulAccessDeniedHandler restfulAccessDeniedHandler = new RestfulAccessDeniedHandler();
        List<String> excludeStaticPatterns = securityProperties.getIgnorePatterns();
        if (CollectionUtils.isEmpty(excludeStaticPatterns)) {
            excludeStaticPatterns = Lists.newArrayList();
        }
        excludeStaticPatterns.addAll(SwaggerStaticResource.EXCLUDE_STATIC_PATTERNS);
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers(ListUtils.listToArray(excludeStaticPatterns)).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .requestMatchers()
                .antMatchers(
                        "/menu/**", "/resource/**",
                        "/role/**", "/user/**",
                        "/common/**", "/org/**",
                        "/station/**", "/application/**",
                        "/tenant/**");
    }
}
