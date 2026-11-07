package com.proveedores.proveedores_api.service;
import com.proveedores.proveedores_api.model.Proveedor;
import com.proveedores.proveedores_api.exception.RegistroNoEncontradoException;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class ProveedorService {
 private final List<Proveedor> registros = new ArrayList<>();
 private long siguienteId = 1;
 public synchronized Proveedor crear(Proveedor entrada){
  Proveedor registro = new Proveedor();
  registro.setId(siguienteId++);
  registro.setNombre(entrada.getNombre());
  registro.setApellido(entrada.getApellido());
  registro.setCorreo(entrada.getCorreo());
  registro.setCategoria(entrada.getCategoria());
  registro.setEstado(entrada.getEstado());
  registros.add(registro);
  return registro;
 }
 public synchronized List<Proveedor> consultar(String filtro){
  return registros.stream().filter(r -> filtro == null || Objects.equals(filtro,r.getCategoria())).toList();
 }
 public synchronized Proveedor consultarPorId(Long id){
  return registros.stream().filter(r -> r.getId().equals(id)).findFirst()
   .orElseThrow(() -> new RegistroNoEncontradoException(id));
 }
}