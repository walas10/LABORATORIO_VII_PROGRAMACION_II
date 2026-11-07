package com.proveedores.proveedores_api.controller;
import com.proveedores.proveedores_api.model.Proveedor;
import com.proveedores.proveedores_api.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/proveedores")
public class ProveedorController {
 private final ProveedorService service;
 public ProveedorController(ProveedorService service){this.service=service;}
 @PostMapping
 public ResponseEntity<Proveedor> crear(@Valid @RequestBody Proveedor entrada){
  Proveedor creado=service.crear(entrada);
  return ResponseEntity.created(URI.create("/proveedores/"+creado.getId())).body(creado);
 }
 @GetMapping
 public ResponseEntity<List<Proveedor>> consultar(@RequestParam(required=false) String categoria){
  return ResponseEntity.ok(service.consultar(categoria));
 }
 @GetMapping("/{id}")
 public ResponseEntity<Proveedor> consultarPorId(@PathVariable Long id){
  return ResponseEntity.ok(service.consultarPorId(id));
 }
}