# 📦 API de Operaciones Dinámicas con Spring Boot, PostgreSQL y Servicio Externo

## 🚀 Requisitos

- **Java 21**
- **Maven 3.8+**
- **Podman** (o Docker)
- **PostgreSQL 16**
- **Node.js** (opcional, solo si deseas levantar el servicio externo localmente)

---

## 📂 Estructura del Proyecto

- **API Principal:** Maneja operaciones aritméticas dinámicas y auditoría de peticiones.
- **Servicio Externo (Node.js):** Genera un número aleatorio.
- **Base de datos (PostgreSQL):** Almacena el historial de requests y responses.
- **Contenerización:** Usamos `podman-compose` o contenedores individuales.

---

## ⚙️ Variables importantes (`application.properties`)

```properties
spring.application.name=api
spring.datasource.url=jdbc:postgresql://localhost:5432/mvp_database
spring.datasource.username=mvp_user
spring.datasource.password=mvp_password
spring.datasource.driver-class-name=org.postgresql.Driver

external.service.url=http://localhost:3000/api/random-number
caching.spring.metricsTTL=1800000
```

---

## 🐘 Base de datos PostgreSQL

- **Base de datos: mvp_database**
- **Usuario: mvp_user**
- **Contraseña: mvp_password**
- **Puerto: 5432**

Inicialización: Se ejecuta un script init.sql que crea las tablas necesarias.

---

## 🛠️ Cómo levantar los servicios
**Opción 1** : Levantar todo (API + Servicio Externo + Base de Datos)

### ⚠️ IMPORTANTE ⚠️ ###
**Se debe generar el JAR del proyecto de springboot**  
```
./mvnw clean package
```
## Para levantar los contenedores: ##

```
podman-compose up
```
## Este comando levantará: ##
- **api:** Spring Boot app en el puerto 8080  
- **external-service** Node.js app en el puerto 3000  
- **postgres-db:** Base de datos PostgreSQL en el puerto 5432  


**Opción 2** : Levantar solo la base de datos (PostgreSQL)  
Si quieres correr el API y el servicio externo localmente, solo levanta la base de datos:

```
podman run --name postgres-db \
-e POSTGRES_USER=mvp_user \
-e POSTGRES_PASSWORD=mvp_password \
-e POSTGRES_DB=mvp_database \
-p 5432:5432 \
-v postgres-data:/var/lib/postgresql/data \
-d docker.io/library/postgres:16
```

## 📡 Endpoints disponibles
# API Spring Boot

**Suma dos números y aplica un porcentaje dinámico basado en el servicio externo**
- `GET /api/v1/metrics?num1=10&num2=20` 

---

**Obtiene el historial de solicitudes y respuestas.**
- `GET` | `/api/v1/history?page=0&size=10&sort=id,desc`  | Obtiene el historial de solicitudes y respuestas con soporte de paginación y ordenamiento. |

#### Parámetros opcionales para `/api/v1/history`

- `page`: Número de página (empezando desde `0`). **Por defecto**: `0`.
- `size`: Número de elementos por página. **Por defecto**: `20`.
- `sort`: Propiedad por la cual ordenar. Puedes ordenar por múltiples campos, por ejemplo:
- `id,,desc` (por id descendente)
- `requestUrl,asc` (por URL ascendente)

**Ejemplos:**

- Obtener la primera página con 10 elementos ordenados por fecha descendente:  
  ``` GET /api/v1/history?page=0&size=10&sort=id,desc ```

# Servicio externo (Node.js)

**Devuelve un número aleatorio entre 1 y 100.**
- `GET	/api/random-number` 

## 🧪 Ejecutar tests
Para correr las pruebas unitarias:

```
mvn clean test
```

para visualizar el reporte se encuentra en la ruta
``` target/site/jacoco/index.html ```


## 📋 Funcionalidades destacadas
# Caching Inteligente:

El porcentaje del servicio externo se cachea por 30 minutos.

Si el servicio externo falla, se usa el último valor cacheado.

# Auditoría Completa:

Se almacenan todas las solicitudes y respuestas en la base de datos.

# Resiliencia:

El API continúa funcionando incluso si el servicio externo está caído.

## 🛠️ Comandos útiles con Podman

**Ver contenedores activos**	```podman ps```  
**Apagar todos los servicios**	```podman-compose down```  
**Ver logs de un contenedor**	```podman logs <nombre-contenedor>```  
**Entrar al contenedor de PostgreSQL**	```podman exec -it postgres-db bash```  
**Conectarse a la base de datos**	```psql -U mvp_user -d mvp_database```  

## 🧹 Comandos manuales sugeridos
Si prefieres controlar los servicios de manera individual:

# Levantar solo el API
```
cd api
./mvnw spring-boot:run
```

# Levantar el servicio externo Node.js
```
cd external-service
node app.js
```

## 📢 Notas importantes
Verifica que los puertos 5432, 8080 y 3000 estén disponibles.

No olvides ajustar el external.service.url en application.properties si corres el servicio externo en otro host o puerto.