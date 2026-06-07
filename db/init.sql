CREATE TABLE IF NOT EXISTS employees (
    id            BIGINT         NOT NULL AUTO_INCREMENT,
    first_name    VARCHAR(100)   NOT NULL,
    last_name     VARCHAR(100)   NOT NULL,
    email         VARCHAR(150)   NOT NULL UNIQUE,
    hire_date     DATE           NOT NULL,
    employee_type VARCHAR(50)    NOT NULL,
    base_salary   DECIMAL(12,2)  NOT NULL,
    vacation_days INT            NOT NULL DEFAULT 14,
    active        BOOLEAN        NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS vacation_requests (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    employee_id    BIGINT       NOT NULL,
    start_date     DATE         NOT NULL,
    end_date       DATE         NOT NULL,
    requested_days INT          NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reason         VARCHAR(500),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bonus_records (
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    employee_id BIGINT         NOT NULL,
    bonus_type  VARCHAR(50)    NOT NULL,
    amount      DECIMAL(12,2)  NOT NULL,
    period      VARCHAR(7)     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS attendance_records (
    id                BIGINT   NOT NULL AUTO_INCREMENT,
    employee_id       BIGINT   NOT NULL,
    record_date       DATE     NOT NULL,
    present           BOOLEAN  NOT NULL DEFAULT TRUE,
    justified_absence BOOLEAN  NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    UNIQUE KEY uq_attendance (employee_id, record_date)
);

INSERT IGNORE INTO employees (id, first_name, last_name, email, hire_date, employee_type, base_salary, vacation_days, active) VALUES
(1, 'Ana',    'Gomez',    'ana.gomez@uda.ar',     '2019-03-01', 'FULL_TIME',  75000.00, 14, TRUE),
(2, 'Carlos', 'Perez',    'carlos.perez@uda.ar',  '2021-06-15', 'PART_TIME',  45000.00, 14, TRUE),
(3, 'Laura',  'Martinez', 'laura.martinez@uda.ar','2018-01-10', 'FULL_TIME',  90000.00, 14, TRUE),
(4, 'Diego',  'Lopez',    'diego.lopez@uda.ar',   '2022-11-20', 'CONTRACTOR', 60000.00, 14, TRUE);
