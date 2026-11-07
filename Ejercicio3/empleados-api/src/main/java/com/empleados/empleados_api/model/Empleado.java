package com.empleados.empleados_api.model;
import jakarta.validation.constraints.*;
public class Empleado {
 private Long id;
 @NotBlank private String nombre;
 @NotBlank private String apellido;
 @NotBlank @Email private String correo;
 private String especialidad;
 private String estado;
 public Long getId(){return id;}
 public void setId(Long v){id=v;}
 public String getNombre(){return nombre;}
 public void setNombre(String v){nombre=v;}
 public String getApellido(){return apellido;}
 public void setApellido(String v){apellido=v;}
 public String getCorreo(){return correo;}
 public void setCorreo(String v){correo=v;}
 public String getEspecialidad(){return especialidad;}
 public void setEspecialidad(String v){especialidad=v;}
 public String getEstado(){return estado;}
 public void setEstado(String v){estado=v;}
}