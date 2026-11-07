package com.empleados.empleados_api.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {
 @Bean
 SecurityFilterChain securityFilterChain(HttpSecurity http,@Value("${lab.api-key}") String key) throws Exception {
  return http.csrf(AbstractHttpConfigurer::disable)
   .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
   .logout(AbstractHttpConfigurer::disable).requestCache(AbstractHttpConfigurer::disable)
   .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a -> a.anyRequest().authenticated())
   .addFilterBefore(new ApiKeyFilter(key),UsernamePasswordAuthenticationFilter.class).build();
 }
}