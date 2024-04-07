package com.j6d7.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Autowired
        private UserService userService;

        @Autowired
        private CustomOAuth2UserService customOAuth2UserService;

        @Bean
        BCryptPasswordEncoder getPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userService);
                authProvider.setPasswordEncoder(getPasswordEncoder());
                return authProvider;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http)
                        throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req
                                                .requestMatchers("/home/admin")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/home/user")
                                                .hasAnyAuthority("USER")
                                                .requestMatchers("/home/auth")
                                                .hasAnyRole("USER", "ADMIN")
                                                .requestMatchers("/home/auth", "/home/user", "/home/admin")
                                                .authenticated()
                                                .anyRequest()
                                                .permitAll())
                                .formLogin(form -> form
                                                .loginPage("/login").permitAll()
                                                .defaultSuccessUrl("/home/index", false))
                                .logout(logout -> logout
                                                .logoutUrl("/logout").permitAll()
                                                .logoutSuccessUrl("/login")
                                                .deleteCookies("JSESSIONID"))
                                .exceptionHandling(e -> e
                                                .accessDeniedPage("/accessDenied")
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .rememberMe(r -> r.tokenValiditySeconds(1 * 24 * 60 * 60))
                                .oauth2Login(ou -> ou.authorizationEndpoint(e -> e
                                                .baseUri("/oauth2/authorization"))
                                                .redirectionEndpoint(e -> e.baseUri("/login/oauth2/code/*"))
                                                .userInfoEndpoint(e -> e.userService(customOAuth2UserService)));
                return http.build();
        }

}