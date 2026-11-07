package com.proveedores.proveedores_api.service;
import com.proveedores.proveedores_api.model.Proveedor;
import com.proveedores.proveedores_api.exception.RegistroNoEncontradoException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ProveedorServiceTest {
 @Test void crearRegistro(){
  // Arrange
  var service=new ProveedorService();
  var entrada=new Proveedor();
  entrada.setId(99L);
  entrada.setNombre("Ana");entrada.setApellido("Lopez");entrada.setCorreo("ana@example.com");
  entrada.setCategoria("Area");entrada.setEstado("ACTIVO");
  // Act
  var creado=service.crear(entrada);
  var segundo=service.crear(entrada);
  // Assert
  assertNotNull(creado.getId());assertNotEquals(99L,creado.getId());
  assertNotEquals(creado.getId(),segundo.getId());
  assertEquals("Ana",creado.getNombre());assertEquals("Lopez",creado.getApellido());
  assertEquals("ana@example.com",creado.getCorreo());assertEquals("Area",creado.getCategoria());
  assertEquals("ACTIVO",creado.getEstado());assertEquals(2,service.consultar(null).size());
 }
 @Test void consultarExistente(){
  // Arrange
  var service=new ProveedorService();var creado=service.crear(new Proveedor());
  // Act
  var encontrado=service.consultarPorId(creado.getId());
  // Assert
  assertSame(creado,encontrado);
 }
 @Test void consultarInexistente(){
  // Arrange
  var service=new ProveedorService();
  // Act
  var error=assertThrows(RegistroNoEncontradoException.class,()->service.consultarPorId(500L));
  // Assert
  assertEquals("Registro no encontrado: 500",error.getMessage());
 }
}