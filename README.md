# 🚗 AutoTrace Enterprise · Sistema de Gestión y Control de Taller Automotriz

Plataforma empresarial de microservicios para la **trazabilidad integral de servicios, vehículos, repuestos y certificaciones de calidad** en talleres automotrices.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje & Framework**: Java 17+ / Spring Boot 3 & 4 (Spring Framework).
- **Persistencia NoSQL**: Spring Data MongoDB con el patrón de diseño **Database-per-Service**.
- **Documentación de APIs**: OpenAPI 3 / Swagger UI (`springdoc-openapi-starter-webmvc-ui`).
- **Capa de Presentación**: Thymeleaf + Bootstrap 5.3 + Bootstrap Icons (Diseño responsivo y dashboards interactivos).
- **Contenerización y Nube**: Dockerfile multi-stage, GitHub y Render Cloud.

---

## 🏛️ Arquitectura: Patrón "Database-per-Service"

Cada módulo/microservicio opera de manera autónoma con su propio modelo de dominio, repositorio, lógica de negocio y base de datos NoSQL MongoDB independiente:

| Microservicio / Módulo | Base de Datos NoSQL | Responsabilidad Principal |
| :--- | :--- | :--- |
| **Administración & Usuarios** | `taller_admin_db` | Usuarios, roles, catálogo de servicios técnicos y catálogo de repuestos del taller. |
| **Clientes & Vehículos** | `taller_clientes_db` | Registro de clientes, parque automotor, VIN, kilometraje y perfiles. |
| **Órdenes & Trazabilidad** | `taller_ordenes_db` | Cotizaciones, órdenes de trabajo, diagnósticos, repuestos utilizados y tiempos de mecánico. |
| **Proveedores & Reabastecimiento**| `taller_proveedores_db` | Empresas proveedoras, catálogo mayorista, órdenes de compra y control de despacho/entrega. |
| **Certificaciones & Auditoría** | `taller_certificaciones_db`| Auditorías de calidad ISO-9001, checklist de seguridad y certificados con hash SHA-256. |

---

## 👥 Roles Empresariales y Flujo de Trabajo

### 1. 👔 Administrador del taller
- **Gestión de Usuarios y Roles**: Altas, bajas y modificaciones (CRUD) para Administradores, Mecánicos, Clientes, Proveedores y Auditores.
- **Catálogo de Servicios**: Gestión de procedimientos (cambio de aceite, frenos, sincronización, etc.) con tarifas base y tiempos estimados.
- **Catálogo de Repuestos**: Control de inventario, precios de venta, stock actual y alertas de stock mínimo.
- **Ruta Web**: `/web/admin`

### 2. 🔧 Mecánico
- **Órdenes Asignadas**: Visualización y filtrado de órdenes de trabajo por estado.
- **Diagnóstico Técnico**: Registro de inspecciones y síntomas detectados.
- **Reparaciones Realizadas**: Detalle de procedimientos mecánicos ejecutados.
- **Repuestos Utilizados**: Registro de insumos utilizados que se descuentan automáticamente del inventario.
- **Tiempos de Servicio**: Registro de horas y minutos de mano de obra invertidos.
- **Ruta Web**: `/web/mecanico`

### 3. 🚗 Cliente
- **Mis Vehículos**: Registro de vehículos (placa, marca, modelo, año, kilometraje, VIN).
- **Cotizaciones y Presupuestos**: Recepción de propuestas con botones para **Aprobar** (crea la Orden de Trabajo automáticamente) o **Rechazar**.
- **Trazabilidad & Hoja de Vida**: Consulta de historial completo de mantenimientos por número de placa en una línea de tiempo.
- **Ruta Web**: `/web/cliente`

### 4. 📦 Proveedor
- **Catálogo Mayorista**: Gestión del inventario de repuestos que suministra y existencias en bodega.
- **Atención de Pedidos**: Recepción de solicitudes de reabastecimiento generadas por el taller.
- **Despachos y Entregas**: Registro de empresa transportadora, número de guía y confirmación de entrega (reabastece el stock del taller).
- **Ruta Web**: `/web/proveedor`

### 5. 📜 Certificador o Auditor
- **Auditoría Técnica**: Inspección de órdenes terminadas contra un checklist de 6 normas de calidad ISO y seguridad vial (frenos, torque, fluidos, escaneo OBD-II, prueba de ruta).
- **Emisión de Certificados**: Generación de certificado digital con puntaje de conformidad y **Hash criptográfico SHA-256** inmutable.
- **Ruta Web**: `/web/auditor` y `/web/auditor/certificado/{id}` (vista imprimible oficial).

---

## 🚀 Ejecución en Spring Tools for Eclipse (STS) / Local

### 1. Clonar o abrir el proyecto en STS:
- Abre **Spring Tool Suite (STS)**.
- Selecciona `File` > `Import` > `Existing Maven Projects`.
- Elige el directorio: `CRUD-Taller-apirest-1`.

### 2. Configurar MongoDB:
En `src/main/resources/application.properties`, define la URI de tu base de datos (local o MongoDB Atlas):
```properties
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017}
```

> **Nota de arranque**: Si aún no tienes MongoDB corriendo al iniciar el proyecto, la aplicación arrancará con normalidad gracias a la inicialización protegida en `DataInitializer`. Cuando MongoDB esté disponible, se crearán y poblarán automáticamente los datos semilla de los 5 roles.

### 3. Ejecutar la aplicación:
- **En STS**: Haz clic derecho en el proyecto > `Run As` > `Spring Boot App`.
- **Por terminal**:
  ```bash
  ./mvnw spring-boot:run
  ```

### 4. Accesos directos en el navegador:
- **Portal Web Principal**: [http://localhost:8080/](http://localhost:8080/)
- **Documentación Swagger / OpenAPI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Especificación OpenAPI (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🌐 Configuración de MongoDB Atlas (Nube Gratuita)

1. Ingresa a [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) y crea una cuenta gratuita.
2. Crea un clúster gratuito **M0 (Shared)**.
3. En **Database Access**, crea un usuario y contraseña (ej: `admin_taller` / `TuPasswordSeguro123`).
4. En **Network Access**, agrega la IP `0.0.0.0/0` (permitir acceso desde cualquier lugar, indispensable para Render).
5. En **Database** > **Connect** > **Drivers (Java)**, copia tu cadena de conexión:
   ```text
   mongodb+srv://admin_taller:<password>@cluster0.abcde.mongodb.net/?retryWrites=true&w=majority
   ```
6. Reemplaza `<password>` con tu contraseña real.

---

## 📤 Despliegue en GitHub y Render Cloud

### Paso 1: Subir el proyecto a GitHub
Abre una terminal en la raíz del proyecto y ejecuta:

```bash
git init
git add .
git commit -m "feat: Sistema de Gestión y Control de Taller con Microservicios y MongoDB"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/CRUD-Taller-apirest.git
git push -u origin main
```

### Paso 2: Desplegar en Render
1. Inicia sesión en [Render.com](https://render.com/).
2. Haz clic en **New +** y selecciona **Web Service**.
3. Conecta tu repositorio de GitHub recién creado.
4. Render detectará automáticamente el archivo `Dockerfile` incluido en el proyecto.
5. Configura los siguientes campos:
   - **Name**: `autotrace-taller-api`
   - **Region**: La más cercana a tu ubicación (ej: Oregon o Frankfurt).
   - **Instance Type**: `Free`.
6. En la sección **Environment Variables**, añade:
   - `MONGODB_URI` = `mongodb+srv://admin_taller:TuPassword@cluster0.abcde.mongodb.net/?retryWrites=true&w=majority`
   - `PORT` = `8080`
7. Haz clic en **Create Web Service**.
8. Render compilará la imagen Docker multi-stage y publicará tu aplicación con un enlace HTTPS público:
   `https://autotrace-taller-api.onrender.com`

---

## 📑 Endpoints RESTful Principales

### 👔 Administrador (`/api/v1/admin`)
- `GET /api/v1/admin/usuarios` - Listar usuarios (filtro opcional por `rol`).
- `POST /api/v1/admin/usuarios` - Crear nuevo usuario.
- `GET /api/v1/admin/servicios` - Listar catálogo de servicios.
- `POST /api/v1/admin/servicios` - Crear nuevo servicio.
- `GET /api/v1/admin/repuestos` - Listar repuestos (filtro opcional `soloBajoStock=true`).
- `POST /api/v1/admin/repuestos` - Crear repuesto.

### 🚗 Clientes y Vehículos (`/api/v1/clientes` & `/api/v1/vehiculos`)
- `GET /api/v1/clientes` - Listar clientes.
- `POST /api/v1/clientes` - Registrar cliente.
- `GET /api/v1/vehiculos` - Listar vehículos (filtro por `clienteId`).
- `GET /api/v1/vehiculos/placa/{placa}` - Buscar vehículo por placa.
- `POST /api/v1/vehiculos` - Registrar vehículo.

### 🔧 Órdenes & Cotizaciones (`/api/v1/ordenes` & `/api/v1/cotizaciones`)
- `GET /api/v1/cotizaciones` - Listar cotizaciones.
- `POST /api/v1/cotizaciones` - Crear cotización.
- `POST /api/v1/cotizaciones/{id}/aprobar` - Aprobar cotización (genera orden de trabajo automáticamente).
- `POST /api/v1/cotizaciones/{id}/rechazar` - Rechazar cotización.
- `GET /api/v1/ordenes` - Listar órdenes de trabajo (filtros por `mecanicoId`, `placa`, `estado`).
- `POST /api/v1/ordenes/{id}/diagnostico` - Registrar diagnóstico técnico.
- `POST /api/v1/ordenes/{id}/reparaciones` - Registrar reparaciones efectuadas.
- `POST /api/v1/ordenes/{id}/repuestos` - Cargar repuestos utilizados a la orden.
- `POST /api/v1/ordenes/{id}/tiempos` - Registrar horas de servicio.
- `PATCH /api/v1/ordenes/{id}/estado` - Cambiar estado de la orden.

### 📦 Proveedores (`/api/v1/proveedores` & `/api/v1/pedidos-reabastecimiento`)
- `GET /api/v1/proveedores` - Listar empresas proveedoras.
- `POST /api/v1/pedidos-reabastecimiento` - Generar solicitud de reabastecimiento.
- `POST /api/v1/pedidos-reabastecimiento/{id}/despachar` - Registrar despacho con guía y transportadora.
- `POST /api/v1/pedidos-reabastecimiento/{id}/entregar` - Confirmar entrega e incrementar stock del taller.

### 📜 Certificaciones & Auditoría (`/api/v1/certificaciones`)
- `GET /api/v1/certificaciones` - Listar certificados emitidos.
- `GET /api/v1/certificaciones/checklist-estandar` - Obtener lista de verificación ISO 9001 y seguridad.
- `POST /api/v1/certificaciones` - Auditar orden y emitir certificado con hash SHA-256.
