package com.github.sparkzxl.authorization.infrastructure.config;

import cn.hutool.core.util.ArrayUtil;
import com.github.sparkzxl.authorization.domain.service.UserDetailsServiceImpl;
import com.github.sparkzxl.authorization.infrastructure.filter.TenantLoginPreFilter;
import com.github.sparkzxl.authorization.infrastructure.security.RestfulAccessDeniedHandler;
import com.github.sparkzxl.authorization.infrastructure.security.SecurityProperties;
import com.github.sparkzxl.authorization.infrastructure.security.filter.PermitAuthenticationFilter;
import com.github.sparkzxl.authorization.infrastructure.security.logout.LogoutSuccessHandlerImpl;
import com.github.sparkzxl.core.resource.SwaggerStaticResource;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * description: 安全认证
 *
 * @author charles.zhou
 * @date   2021-02-23 14:19:05
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private SecurityProperties securityProperties;

    @Autowired
    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TenantLoginPreFilter tenantLoginPreFilter() {
        return new TenantLoginPreFilter();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PermitAuthenticationFilter permitAuthenticationFilter() {
        return new PermitAuthenticationFilter();
    }

    @Bean
    public PermitAllSecurityConfig permitAllSecurityConfig() {
        PermitAllSecurityConfig permitAllSecurityConfig = new PermitAllSecurityConfig();
        permitAllSecurityConfig.setPermitAuthenticationFilter(permitAuthenticationFilter());
        return permitAllSecurityConfig;

    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RestfulAccessDeniedHandler restfulAccessDeniedHandler = new RestfulAccessDeniedHandler();
        List<String> ignorePatternList = securityProperties.getIgnorePatterns();
        if (CollectionUtils.isNotEmpty(ignorePatternList)) {
            http.authorizeRequests()
                    .antMatchers(ArrayUtil.toArray(ignorePatternList, String.class)).permitAll();
        }
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll();
        if (!securityProperties.isCsrf()) {
            http.csrf().disable();
        }
        http.logout().logoutUrl("/customLogout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/form")
                .permitAll().and()
                .httpBasic()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .and()
                .addFilterBefore(tenantLoginPreFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) {
        List<String> ignoreStaticPatterns = Lists.newArrayList();
        ignoreStaticPatterns.addAll(SwaggerStaticResource.EXCLUDE_STATIC_PATTERNS);
        if (CollectionUtils.isNotEmpty(securityProperties.getIgnoreStaticPatterns())) {
            ignoreStaticPatterns.addAll(securityProperties.getIgnoreStaticPatterns());
        }
        web.ignoring().antMatchers(ArrayUtil.toArray(ignoreStaticPatterns, String.class));
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        web.httpFirewall(firewall);
    }

}
