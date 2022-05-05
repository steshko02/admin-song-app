package com.epam.adminapp.config;

import com.auth0.client.auth.AuthAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
class AuthConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation("https://dev-hf6j43cb.eu.auth0.com/");
    }

    @Bean
    public AuthAPI authAPI() {
        return new AuthAPI("dev-hf6j43cb.eu.auth0.com", "cZixQcgCJOj4m0xwGdEoJ7y0EfwTIDXL",
                "zajeDbViNQNoa-Jbpb73yY2p3nOV6HcR8HTY0IsRugMjbedoIk4F7JMImd_F4jo9");
    }
}