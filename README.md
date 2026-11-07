# Laboratorio VII - Programacion II

Tres APIs Maven existentes, con Java 25 y Spring Boot 4.1.1. Cada una contiene model, controller, service, exception y security. Los registros viven en una lista en memoria y se pierden al reiniciar; el servidor asigna IDs numericos consecutivos aunque el cliente envie un id.

| Proyecto | Puerto | Recurso | Filtro GET |
|---|---:|---|---|
| Ejercicio1/profesores-api | 8081 | /profesores | especialidad=Matematicas |
| Ejercicio2/proveedores-api | 8082 | /proveedores | categoria=Tecnologia |
| Ejercicio3/empleados-api | 8083 | /empleados | especialidad=Sistemas |

## Iniciar

En PowerShell, desde la raiz, ejecutar cada aplicacion en una terminal diferente. Definir LAB_API_KEY en cada terminal; nunca escribir la clave real en archivos versionados.

```powershell
$env:LAB_API_KEY = Read-Host 'API Key local'
mvn -f Ejercicio1/profesores-api/pom.xml spring-boot:run
```

Para proveedores y empleados usar respectivamente:

```powershell
mvn -f Ejercicio2/proveedores-api/pom.xml spring-boot:run
mvn -f Ejercicio3/empleados-api/pom.xml spring-boot:run
```

Tambien se puede iniciar el JAR generado con `java -jar`, por ejemplo:

```powershell
java -jar Ejercicio1/profesores-api/target/profesores-api-0.0.1-SNAPSHOT.jar
```

La configuracion `lab.api-key=${LAB_API_KEY}` exige la variable al iniciar y rechaza valores vacios. ApiKeyFilter se instala solamente dentro de SecurityFilterChain: valida X-API-KEY y crea la autenticacion antes de autorizar el acceso al controlador. Se desactivan Basic, formulario, logout, request cache y sesiones; tampoco se genera un usuario por defecto. No hay persistencia, frontend ni Swagger. Hibernate Validator es la implementacion de Bean Validation, no es JPA/Hibernate ORM.

## Probar manualmente

En otra terminal, ingresar la misma clave:

```powershell
$headers = @{ 'X-API-KEY' = (Read-Host 'API Key local') }
$base = 'http://localhost:8081/profesores'
$body = @{
    nombre = 'Ana'
    apellido = 'Lopez'
    correo = 'ana@example.com'
    especialidad = 'Matematicas'
    estado = 'ACTIVO'
} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri $base -Headers $headers -ContentType 'application/json' -Body $body
Invoke-RestMethod -Uri $base -Headers $headers
Invoke-RestMethod -Uri "${base}?especialidad=Matematicas" -Headers $headers
Invoke-RestMethod -Uri "$base/1" -Headers $headers
```

Para proveedores usar `http://localhost:8082/proveedores` y sustituir `especialidad` por `categoria`, con valor `Tecnologia`. Para empleados usar `http://localhost:8083/empleados`, con `especialidad=Sistemas`.

Respuestas: POST valido 201 (incluye Location), GET valido 200, datos invalidos/JSON incorrecto/id no numerico 400, id inexistente 404, clave ausente o incorrecta 401. Nombre y apellido requieren @NotBlank; correo requiere @NotBlank y @Email. Los filtros comparan el valor exacto; sin coincidencias devuelven una lista vacia. Especialidad/categoria y estado son cadenas libres porque no se especificaron restricciones adicionales.

## Pruebas y empaquetado

```powershell
foreach ($project in @('Ejercicio1/profesores-api','Ejercicio2/proveedores-api','Ejercicio3/empleados-api')) {
    mvn -f "$project/pom.xml" test
    mvn -f "$project/pom.xml" package
}
```

Cada proyecto tiene tres pruebas unitarias JUnit con Arrange, Act, Assert: crear registro (incluye IDs distintos), consultar existente y consultar inexistente. Los reportes quedan en `target/surefire-reports` y los JAR en `target`.

En este equipo se resolvio el error PKIX usando los certificados de confianza de Windows, sin desactivar TLS. Si se repite en Windows, antes de Maven:

```powershell
$env:MAVEN_OPTS = '-Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE'
```

Verificacion HTTP reproducible, con puertos 8081-8083 libres y los JAR ya empaquetados:

```powershell
./verificacion/verificar-http.ps1
```

El script inicia y detiene cada JAR, genera una clave efimera, prueba los codigos 201/200/400/404/401, IDs automaticos y filtros con y sin coincidencias. No modifica registros de otras aplicaciones. Sus logs quedan en `verificacion`.

## Archivos creados y modificados

En cada paquete base `src/main/java/com/<recurso>/<recurso>_api` se crearon:

- `model/Profesor.java`, `model/Proveedor.java` o `model/Empleado.java`.
- `controller/ProfesorController.java`, `controller/ProveedorController.java` o `controller/EmpleadoController.java`.
- `service/ProfesorService.java`, `service/ProveedorService.java` o `service/EmpleadoService.java`.
- `exception/RegistroNoEncontradoException.java` y `exception/GlobalExceptionHandler.java`.
- `security/ApiKeyFilter.java` y `security/SecurityConfig.java`.

En `src/test/java` de cada proyecto se sustituyo la prueba de contexto generada por `service/ProfesorServiceTest.java`, `service/ProveedorServiceTest.java` o `service/EmpleadoServiceTest.java` (tres pruebas cada clase).

Se modificaron los tres `pom.xml` (Boot 4.1.1, Java 25, dependencias web MVC, validacion, seguridad y pruebas; eliminada governance-starter) y los tres `application.properties` (puerto, API Key de entorno y desactivacion del usuario automatico). Se conservaron las clases Application existentes.

Tambien se crearon este README y `verificacion/verificar-http.ps1`. No se ejecutaron comandos Git.
