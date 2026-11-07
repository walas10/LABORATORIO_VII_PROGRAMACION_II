package com.empleados.empleados_api.controller;
import com.empleados.empleados_api.model.Empleado;
import com.empleados.empleados_api.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
 private final EmpleadoService service;
 public EmpleadoController(EmpleadoService service){this.service=service;}
 @PostMapping
 public ResponseEntity<Empleado> crear(@Valid @RequestBody Empleado entrada){
  Empleado creado=service.crear(entrada);
  return ResponseEntity.created(URI.create("/empleados/"+creado.getId())).body(creado);
 }
 @GetMapping
 public ResponseEntity<List<Empleado>> consultar(@RequestParam(required=false) String especialidad){
  return ResponseEntity.ok(service.consultar(especialidad));
 }
 @GetMapping("/{id}")
 public ResponseEntity<Empleado> consultarPorId(@PathVariable Long id){
  return ResponseEntity.ok(service.consultarPorId(id));
 }
}