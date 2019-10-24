package com.gdn.onestop.web.config;

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
    OsUserDetailsService userDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CorsFilter corsFilter;

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
                        "/download/**")
                .permitAll()
                .anyRequest()
//                .permitAll()
                .authenticated()
                .and()
                .exceptionHandling()
                .and()
                    .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
        ;


    }

}
