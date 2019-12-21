package com.gdn.onestop.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.impl.OsUserDetailsService;
import com.gdn.onestop.web.config.filter.CorsFilter;
import com.gdn.onestop.web.config.jwt.JwtConfigurer;
import com.gdn.onestop.web.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

//https://www.codementor.io/gtommee97/rest-authentication-with-spring-security-and-mongodb-j8wgh8kg7
//https://www.baeldung.com/securing-a-restful-web-service-with-spring-security

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CorsFilter corsFilter;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/*",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/docs",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/public/**",
                        "/download/**",
                        "/book/**",
                        "/audio/**")
                .permitAll()
                .anyRequest()
//                .permitAll()
                .authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    Response response = new Response();
                    response.setStatus("Unauthorized");
                    response.setCode(401);
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
                    httpServletResponse.setStatus(401);
                    httpServletResponse.setHeader("content-type","application/json");
                }).accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                    Response response = new Response();
                    response.setStatus("Forbidden");
                    response.setCode(403);
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
                    httpServletResponse.setStatus(403);
                    httpServletResponse.setHeader("content-type","application/json");
                })
                .and()
                    .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
        ;


    }

}
