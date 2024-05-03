package com.codegym.springsecurity.bai15_springsecurity.security;

import com.codegym.springsecurity.bai15_springsecurity.controller.CustomSuccessHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}12345").roles("USER")
                .and()
                .withUser("admin").password("{noop}12345").roles("ADMIN")
                .and()
                .withUser("dba").password("{noop}12345").roles("ADMIN","DBA");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/", "/home").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/dba/**").hasAnyRole("ADMIN", "DBA")
                .and()
                .formLogin().successHandler(new CustomSuccessHandler())
                .usernameParameter("ssoId").passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().exceptionHandling().accessDeniedPage("/accessDenied");
    }
}
