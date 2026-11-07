package com.profesores.profesores_api.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(RegistroNoEncontradoException.class)
 public ResponseEntity<Map<String,String>> noEncontrado(RegistroNoEncontradoException e){
  return ResponseEntity.status(404).body(Map.of("mensaje",e.getMessage()));
 }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<Map<String,Object>> validacion(MethodArgumentNotValidException e){
  Map<String,String> errores=new LinkedHashMap<>();
  e.getBindingResult().getFieldErrors().forEach(f -> errores.put(f.getField(),f.getDefaultMessage()));
  return ResponseEntity.badRequest().body(Map.of("mensaje","Datos invalidos","errores",errores));
 }
 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class})
 public ResponseEntity<Map<String,String>> solicitudInvalida(Exception e){
  return ResponseEntity.badRequest().body(Map.of("mensaje","JSON o parametro invalido"));
 }
}