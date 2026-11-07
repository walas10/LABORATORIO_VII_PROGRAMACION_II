package com.proveedores.proveedores_api.model;
import jakarta.validation.constraints.*;
public class Proveedor {
 private Long id;
 @NotBlank private String nombre;
 @NotBlank private String apellido;
 @NotBlank @Email private String correo;
 private String categoria;
 private String estado;
 public Long getId(){return id;}
 public void setId(Long v){id=v;}
 public String getNombre(){return nombre;}
 public void setNombre(String v){nombre=v;}
 public String getApellido(){return apellido;}
 public void setApellido(String v){apellido=v;}
 public String getCorreo(){return correo;}
 public void setCorreo(String v){correo=v;}
 public String getCategoria(){return categoria;}
 public void setCategoria(String v){categoria=v;}
 public String getEstado(){return estado;}
 public void setEstado(String v){estado=v;}
}