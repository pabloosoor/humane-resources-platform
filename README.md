# HR Platform — UDA

Plataforma de recursos humanos desarrollada en Java 17 con JDBC puro y MySQL. Aplicación de consola con arquitectura multi-módulo Gradle.

Aplica los principios SOLID:
- **OCP (Open/Closed Principle):** las reglas de vacaciones, bonos y presentismo son interfaces. Para agregar una nueva regla se crea una nueva clase sin modificar las existentes.
- **SRP (Single Responsibility Principle):** cada clase tiene una única razón de cambio. El controlador de menú no calcula, los servicios no persisten, los repositorios no tienen lógica de negocio.

---

## Requisitos

- Java 17
- Docker Desktop
- IntelliJ IDEA (recomendado)
- MySQL Workbench (opcional, para inspeccionar la base de datos)

---

## Levantar la aplicación desde cero

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd humane-resources-platform
```

### 2. Levantar la base de datos con Docker

```bash
docker compose up -d
```

Esto descarga la imagen de MySQL 8, crea la base `humane_resources`, las tablas y carga datos de prueba automáticamente desde `db/init.sql`.

Para verificar que el contenedor está corriendo:

```bash
docker compose ps
```

El estado debe decir `healthy`.

### 3. Verificar la configuración de base de datos

El archivo `backend/src/main/resources/db.properties` contiene la conexión:

```properties
db.url=jdbc:mysql://localhost:3307/humane_resources
db.user=hr_user
db.password=hr_password
```

El puerto es `3307` (no el 3306 estándar) para no colisionar con instalaciones locales de MySQL.

### 4. Abrir en IntelliJ IDEA

- `File → Open` → seleccionar la carpeta raíz del proyecto
- IntelliJ detecta el `build.gradle` y pregunta si abrir como proyecto Gradle → confirmar
- En el panel `Gradle` → hacer click en el ícono de recarga para descargar dependencias

### 5. Correr la aplicación

Navegar a `backend/src/main/java/com/uda/backend/Main.java` → click derecho → **Run 'Main.main()'**

---

## Uso del menú

Al correr la aplicación aparece:

```
========================================
         HR Platform — UDA
========================================
  1. Vacaciones
  2. Bonos
  3. Presentismo
  4. Ver empleados
  0. Salir
  Opción:
```

### Vacaciones

Permite solicitar días de vacaciones para un empleado. El sistema valida si tiene saldo disponible y registra la solicitud como APROBADA o RECHAZADA. Si es aprobada, descuenta los días del saldo del empleado en la base de datos.

### Bonos

Calcula el bono por antigüedad: `años trabajados x 1% del salario base`. Valida que no se calcule dos veces el mismo bono para el mismo período.

### Presentismo

Primero registrar asistencias del mes con la opción 2 del submenú. Luego calcular con la opción 1. La regla es todo o nada: sin ausencias injustificadas en el mes el empleado recibe el 10% del salario base. Con al menos una ausencia injustificada no recibe bono.

---

## Comandos Docker útiles

```bash
docker compose up -d        # levantar contenedor
docker compose down         # bajar (los datos persisten)
docker compose down -v      # bajar y borrar todos los datos
docker compose logs -f      # ver logs en tiempo real
```

---

## Conexión con MySQL Workbench

| Campo    | Valor               |
|----------|---------------------|
| Hostname | 127.0.0.1           |
| Port     | 3307                |
| Username | hr_user             |
| Password | hr_password         |
| Schema   | humane_resources    |

---

## Estructura del proyecto

```
humane-resources-platform/
├── docker-compose.yml
├── db/
│   └── init.sql                  schema y datos de prueba
├── access-data/                  modulo de definiciones (sin logica)
│   └── src/main/java/com/uda/accessdata/
│       ├── employee/
│       │   ├── Employee.java           POJO del empleado
│       │   ├── EmployeeType.java       enum FULL_TIME / PART_TIME / CONTRACTOR
│       │   └── EmployeeRepository.java interfaz de acceso a datos
│       ├── vacation/
│       │   ├── VacationRequest.java    POJO de solicitud
│       │   ├── VacationStatus.java     enum PENDIENTE / APROBADA / RECHAZADA
│       │   ├── VacationRepository.java interfaz de acceso a datos
│       │   ├── VacationResult.java     record con el resultado de la operacion
│       │   └── VacationPolicy.java     interfaz OCP para reglas de vacaciones
│       ├── bonus/
│       │   ├── BonusRecord.java        POJO del registro de bono
│       │   ├── BonusType.java          enum ANTIGUEDAD / PRESENTISMO / PERFORMANCE
│       │   ├── BonusRepository.java    interfaz de acceso a datos
│       │   ├── BonusResult.java        record con el resultado del calculo
│       │   └── BonusCalculator.java    interfaz OCP para calculadoras de bono
│       └── presentismo/
│           ├── AttendanceRecord.java       POJO de asistencia
│           ├── AttendanceRepository.java   interfaz de acceso a datos
│           ├── PresentismoResult.java      record con el resultado
│           └── PresentismoCalculator.java  interfaz OCP para la regla
└── backend/                      modulo que ejecuta y conecta todo
    └── src/main/java/com/uda/backend/
        ├── Main.java                     punto de entrada, ensambla dependencias
        ├── config/
        │   └── ConnectionManager.java    maneja la conexion JDBC
        ├── employee/
        │   ├── EmployeeJdbcRepository.java  implementacion JDBC
        │   └── EmployeeService.java         logica de consulta de empleados
        ├── vacation/
        │   ├── VacationJdbcRepository.java
        │   ├── VacationService.java
        │   ├── VacationPolicyRegistry.java  registra las politicas disponibles
        │   └── policy/
        │       └── StandardVacationPolicy.java
        ├── bonus/
        │   ├── BonusJdbcRepository.java
        │   ├── BonusService.java
        │   ├── BonusCalculatorRegistry.java
        │   └── calculator/
        │       └── AntiguedadBonusCalculator.java
        ├── presentismo/
        │   ├── AttendanceJdbcRepository.java
        │   ├── PresentismoService.java
        │   └── calculator/
        │       └── StandardPresentismoCalculator.java
        └── menu/
            └── MenuRunner.java           menu de terminal
```
