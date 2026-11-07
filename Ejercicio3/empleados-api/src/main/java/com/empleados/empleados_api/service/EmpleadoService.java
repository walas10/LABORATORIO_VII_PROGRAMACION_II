package com.empleados.empleados_api.service;
import com.empleados.empleados_api.model.Empleado;
import com.empleados.empleados_api.exception.RegistroNoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class EmpleadoService {
 private final List<Empleado> registros = new ArrayList<>();
 private long siguienteId = 1;
 public synchronized Empleado crear(Empleado entrada){
  Empleado registro = new Empleado();
  registro.setId(siguienteId++);
  registro.setNombre(entrada.getNombre());
  registro.setApellido(entrada.getApellido());
  registro.setCorreo(entrada.getCorreo());
  registro.setEspecialidad(entrada.getEspecialidad());
  registro.setEstado(entrada.getEstado());
  registros.add(registro);
  return registro;
 }
 public synchronized List<Empleado> consultar(String filtro){
  return registros.stream().filter(r -> filtro == null || Objects.equals(filtro,r.getEspecialidad())).toList();
 }
 public synchronized Empleado consultarPorId(Long id){
  return registros.stream().filter(r -> r.getId().equals(id)).findFirst()
   .orElseThrow(() -> new RegistroNoEncontradoException(id));
 }
}