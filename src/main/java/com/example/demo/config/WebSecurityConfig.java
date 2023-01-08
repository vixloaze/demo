package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.OAuthSuccessHandler;
import com.example.demo.security.OAuthUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserService; // 우리가 만든 OAuthUserServiceImpl 추가

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler; // Success Handler 추가

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http 시큐리티 빌더
        http.cors() //WebMvgConfig에서 이미 설정했으므로 기본 cors 설정
                .and()
                .csrf() // csrf는 현재 사용하지 않으므로 disable
                    .disable()
                .httpBasic()// token을 사용하므로 기본 인증 disable
                    .disable()
                .sessionManagement() // session 기반이 아님을 선언
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // /와 /auth/** 경로는 인증 안 해도 됨
                    .antMatchers("/", "/auth/**","/oauth2/**").permitAll() // oauth2 엔드포인트 추가
                .anyRequest() // /와 /auth/** 경로 이외의 모든 경로는 인증해야 함
                    .authenticated()
                            .and()
                                    .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*") // callback uri 설정 // oauth2Login 설정
                .and()
                        .userInfoEndpoint()
                                .userService(oAuthUserService) // OAuthUserServiceImpl를 유저 서비스로 등록
                .and()
                        .successHandler(oAuthSuccessHandler) // Success Handler 등록
                .and()
                        .exceptionHandling()
                                .authenticationEntryPoint(new Http403ForbiddenEntryPoint()); // Http403ForbiddenEntryPoint 추가

        // filter 등록
        // 매 요청마다
        // CorsFilter 실행한 후에
        // JwtAuthenticationFilter 실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
    }
}
