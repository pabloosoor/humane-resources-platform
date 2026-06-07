CREATE DATABASE IF NOT EXISTS humane_resources;
USE humane_resources;

-- --------------------------------------------------------
-- EMPLOYEES
-- --------------------------------------------------------
CREATE TABLE employees (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(100)    NOT NULL,
    apellido         VARCHAR(100)    NOT NULL,
    email            VARCHAR(150)    NOT NULL UNIQUE,
    fecha_ingreso    DATE            NOT NULL,
    tipo_empleado    ENUM(
                         'FULL_TIME',
                         'PART_TIME',
                         'CONTRACTOR'
                     )               NOT NULL,
    salario_base     DECIMAL(12, 2)  NOT NULL,
    dias_vacaciones  INT             NOT NULL DEFAULT 14,
    activo           BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- --------------------------------------------------------
-- VACATION REQUESTS
-- --------------------------------------------------------
CREATE TABLE vacation_requests (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    employee_id      BIGINT          NOT NULL,
    fecha_inicio     DATE            NOT NULL,
    fecha_fin        DATE            NOT NULL,
    dias_solicitados INT             NOT NULL,
    estado           ENUM(
                         'PENDIENTE',
                         'APROBADA',
                         'RECHAZADA'
                     )               NOT NULL DEFAULT 'PENDIENTE',
    motivo           VARCHAR(255),
    created_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_vacation_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id)
        ON DELETE CASCADE
);

-- --------------------------------------------------------
-- BONUS RECORDS
-- --------------------------------------------------------
CREATE TABLE bonus_records (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    employee_id      BIGINT          NOT NULL,
    tipo_bono        ENUM(
                         'ANTIGUEDAD',
                         'PRESENTISMO',
                         'PERFORMANCE'
                     )               NOT NULL,
    monto            DECIMAL(12, 2)  NOT NULL,
    periodo          VARCHAR(7)      NOT NULL,  -- formato: YYYY-MM
    created_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_bonus_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id)
        ON DELETE CASCADE
);

-- --------------------------------------------------------
-- ATTENDANCE RECORDS (para presentismo)
-- --------------------------------------------------------
CREATE TABLE attendance_records (
    id                   BIGINT      NOT NULL AUTO_INCREMENT,
    employee_id          BIGINT      NOT NULL,
    fecha                DATE        NOT NULL,
    presente             BOOLEAN     NOT NULL DEFAULT TRUE,
    ausencia_justificada BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_attendance_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_attendance_employee_fecha
        UNIQUE (employee_id, fecha)
);

-- --------------------------------------------------------
-- SEED DATA (empleados de prueba)
-- --------------------------------------------------------
INSERT INTO employees (nombre, apellido, email, fecha_ingreso, tipo_empleado, salario_base, dias_vacaciones)
VALUES
    ('Carlos',   'García',   'carlos.garcia@uda.edu.ar',   '2019-03-15', 'FULL_TIME',  150000.00, 21),
    ('Lucía',    'Martínez', 'lucia.martinez@uda.edu.ar',  '2023-07-01', 'FULL_TIME',  120000.00, 14),
    ('Marcos',   'López',    'marcos.lopez@uda.edu.ar',    '2015-01-10', 'PART_TIME',   80000.00, 28),
    ('Valentina','Ríos',     'valentina.rios@uda.edu.ar',  '2024-02-20', 'CONTRACTOR',  95000.00, 14);
