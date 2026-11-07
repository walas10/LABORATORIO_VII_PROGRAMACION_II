package com.profesores.profesores_api.controller;
import com.profesores.profesores_api.model.Profesor;
import com.profesores.profesores_api.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/profesores")
public class ProfesorController {
 private final ProfesorService service;
 public ProfesorController(ProfesorService service){this.service=service;}
 @PostMapping
 public ResponseEntity<Profesor> crear(@Valid @RequestBody Profesor entrada){
  Profesor creado=service.crear(entrada);
  return ResponseEntity.created(URI.create("/profesores/"+creado.getId())).body(creado);
 }
 @GetMapping
 public ResponseEntity<List<Profesor>> consultar(@RequestParam(required=false) String especialidad){
  return ResponseEntity.ok(service.consultar(especialidad));
 }
 @GetMapping("/{id}")
 public ResponseEntity<Profesor> consultarPorId(@PathVariable Long id){
  return ResponseEntity.ok(service.consultarPorId(id));
 }
}