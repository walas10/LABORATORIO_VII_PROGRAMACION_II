package com.profesores.profesores_api.service;
import com.profesores.profesores_api.model.Profesor;
import com.profesores.profesores_api.exception.RegistroNoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class ProfesorService {
 private final List<Profesor> registros = new ArrayList<>();
 private long siguienteId = 1;
 public synchronized Profesor crear(Profesor entrada){
  Profesor registro = new Profesor();
  registro.setId(siguienteId++);
  registro.setNombre(entrada.getNombre());
  registro.setApellido(entrada.getApellido());
  registro.setCorreo(entrada.getCorreo());
  registro.setEspecialidad(entrada.getEspecialidad());
  registro.setEstado(entrada.getEstado());
  registros.add(registro);
  return registro;
 }
 public synchronized List<Profesor> consultar(String filtro){
  return registros.stream().filter(r -> filtro == null || Objects.equals(filtro,r.getEspecialidad())).toList();
 }
 public synchronized Profesor consultarPorId(Long id){
  return registros.stream().filter(r -> r.getId().equals(id)).findFirst()
   .orElseThrow(() -> new RegistroNoEncontradoException(id));
 }
}