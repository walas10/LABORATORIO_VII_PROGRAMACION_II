package com.empleados.empleados_api.exception;
public class RegistroNoEncontradoException extends RuntimeException {
 public RegistroNoEncontradoException(Long id){super("Registro no encontrado: " + id);}
}