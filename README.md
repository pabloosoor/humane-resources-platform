# HR Platform — UDA

REST API de recursos humanos desarrollada en Java 17 puro con JDBC y MySQL. Sin frameworks — usa el `HttpServer` del JDK y Gson para JSON.

Aplica principios SOLID:
- **OCP:** las reglas de vacaciones y bonos son interfaces (`VacationPolicy`, `BonusCalculator`). Para agregar una nueva regla se crea una nueva clase sin modificar las existentes.
- **SRP:** cada clase tiene una única responsabilidad. El controller maneja HTTP, el service orquesta la lógica, el repository persiste datos.

---

## Requisitos

- Java 17
- Docker Desktop
- IntelliJ IDEA

---

## Levantar la aplicación

### 1. Clonar el repositorio

```bash
git clone https://github.com/pabloosoor/humane-resources-platform.git
cd humane-resources-platform
```

### 2. Levantar MySQL con Docker

```bash
docker compose up -d
```

Crea la base `humane_resources`, las tablas y carga 4 empleados de prueba desde `db/init.sql`.

Para verificar que está listo:

```bash
docker compose ps
```

El estado debe decir `healthy`.

### 3. Abrir en IntelliJ IDEA

- `File → Open` → seleccionar la carpeta raíz del proyecto
- IntelliJ detecta el `build.gradle` → confirmar como proyecto Gradle
- Panel `Gradle` → botón de recarga para descargar dependencias (Gson + MySQL connector)

### 4. Correr la aplicación

Navegar a `src/main/java/com/uda/hrplatform/Main.java` → click derecho → **Run 'Main.main()'**

La app queda escuchando en `http://localhost:8080`.

---

## Endpoints

### Employees

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/employees` | Listar empleados activos |
| GET | `/api/employees/{id}` | Obtener empleado por ID |
| POST | `/api/employees` | Crear empleado |
| PUT | `/api/employees/{id}` | Actualizar empleado |
| DELETE | `/api/employees/{id}` | Desactivar empleado (soft delete) |

**POST /api/employees — body:**
```json
{
  "firstName": "Ana",
  "lastName": "Gomez",
  "email": "ana@uda.ar",
  "hireDate": "2020-01-15",
  "employeeType": "FULL_TIME",
  "baseSalary": 75000.00
}
```

**PUT /api/employees/{id} — body (solo los campos a cambiar):**
```json
{
  "baseSalary": 85000.00
}
```

---

### Vacations

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/employees/{id}/vacations` | Solicitar vacaciones |
| GET | `/api/employees/{id}/vacations` | Historial de vacaciones |

**POST body:**
```json
{
  "startDate": "2025-02-01",
  "endDate": "2025-02-10",
  "requestedDays": 10,
  "reason": "Annual leave"
}
```

---

### Bonus

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/employees/{id}/bonus` | Calcular bono |
| GET | `/api/employees/{id}/bonus` | Historial de bonos |

**POST body:**
```json
{
  "bonusType": "SENIORITY",
  "period": "2025-01"
}
```

`bonusType` puede ser: `SENIORITY`, `ATTENDANCE_BONUS`, `PERFORMANCE`.

---

### Attendance

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/employees/{id}/attendance` | Registrar asistencia de un día |
| GET | `/api/employees/{id}/attendance?period=2025-01` | Ver asistencias del período |
| POST | `/api/employees/{id}/attendance/bonus?period=2025-01` | Calcular bono de presentismo |

**POST /attendance body:**
```json
{
  "date": "2025-01-15",
  "present": true,
  "justifiedAbsence": false
}
```

---

## Reglas de negocio

**Vacaciones:** valida que el empleado tenga saldo disponible. Si se aprueba, descuenta los días automáticamente.

**Bono por antigüedad:** `años trabajados × 1% del salario base`. No se puede calcular dos veces el mismo período.

**Bono de presentismo:** 10% del salario base si no hay ausencias injustificadas en el mes. Con una o más ausencias injustificadas el bono es cero.

---

## Comandos Docker útiles

```bash
docker compose up -d        # levantar
docker compose down         # bajar (datos persisten)
docker compose down -v      # bajar y borrar todos los datos
docker compose logs -f      # ver logs
```

---

## Conexión con MySQL Workbench

| Campo    | Valor            |
|----------|------------------|
| Hostname | 127.0.0.1        |
| Port     | 3307             |
| Username | hr_user          |
| Password | hr_password      |
| Schema   | humane_resources |

---

## Estructura del proyecto

```
src/main/java/com/uda/hrplatform/
├── Main.java                        punto de entrada, ensambla dependencias y rutas
├── model/                           POJOs y enums del dominio
│   ├── Employee.java
│   ├── EmployeeType.java            FULL_TIME | PART_TIME | CONTRACTOR
│   ├── VacationRequest.java
│   ├── VacationStatus.java          PENDING | APPROVED | REJECTED
│   ├── VacationApproval.java        resultado interno de la política
│   ├── BonusRecord.java
│   ├── BonusType.java               SENIORITY | ATTENDANCE_BONUS | PERFORMANCE
│   └── AttendanceRecord.java
├── dto/                             objetos de entrada de la API
│   ├── CreateEmployeeRequest.java
│   ├── UpdateEmployeeRequest.java
│   ├── NewVacationRequest.java
│   ├── CalculateBonusRequest.java
│   └── RegisterAttendanceRequest.java
├── repository/                      interfaces de acceso a datos
│   ├── EmployeeRepository.java
│   ├── VacationRepository.java
│   ├── BonusRepository.java
│   ├── AttendanceRepository.java
│   └── impl/                        implementaciones JDBC
│       ├── EmployeeJdbcRepository.java
│       ├── VacationJdbcRepository.java
│       ├── BonusJdbcRepository.java
│       └── AttendanceJdbcRepository.java
├── service/                         lógica de negocio
│   ├── EmployeeService.java
│   ├── VacationService.java
│   ├── BonusService.java
│   ├── AttendanceBonusService.java
│   ├── policy/                      OCP — reglas de vacaciones
│   │   ├── VacationPolicy.java      interfaz
│   │   └── StandardVacationPolicy.java
│   └── calculator/                  OCP — cálculo de bonos
│       ├── BonusCalculator.java     interfaz
│       ├── SeniorityBonusCalculator.java
│       ├── AttendanceBonusCalculator.java  interfaz
│       └── StandardAttendanceBonusCalculator.java
├── controller/                      manejo de requests HTTP
│   ├── EmployeeController.java
│   ├── VacationController.java
│   ├── BonusController.java
│   └── AttendanceController.java
└── utils/
    ├── ConnectionManager.java       conexión JDBC desde db.properties
    ├── Router.java                  enrutador con soporte de path variables
    └── HttpUtils.java               helpers JSON (Gson) y query params
```
