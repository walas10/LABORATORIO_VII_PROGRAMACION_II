package com.profesores.profesores_api.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
public class ApiKeyFilter extends OncePerRequestFilter {
 private final byte[] expected;
 public ApiKeyFilter(String key){
  if(key.isBlank()) throw new IllegalArgumentException("Debe configurar LAB_API_KEY");
  expected=key.getBytes(StandardCharsets.UTF_8);
 }
 @Override
 protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
   throws ServletException,IOException {
  String key=request.getHeader("X-API-KEY");
  if(key==null || !MessageDigest.isEqual(expected,key.getBytes(StandardCharsets.UTF_8))){
   response.setStatus(401);
   response.setContentType("application/json");
   response.setCharacterEncoding("UTF-8");
   response.getWriter().write("{\"mensaje\":\"API Key invalida o ausente\"}");
   return;
  }
  var context=SecurityContextHolder.createEmptyContext();
  context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated("api-client",null,AuthorityUtils.NO_AUTHORITIES));
  SecurityContextHolder.setContext(context);
  chain.doFilter(request,response);
 }
}