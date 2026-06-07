INSERT IGNORE INTO employees (id, first_name, last_name, email, hire_date, employee_type, base_salary, vacation_days, active)
VALUES
    (1, 'Ana',    'Gomez',    'ana.gomez@uda.ar',     '2019-03-01', 'FULL_TIME',   75000.00, 14, true),
    (2, 'Carlos', 'Perez',    'carlos.perez@uda.ar',  '2021-06-15', 'PART_TIME',   45000.00, 14, true),
    (3, 'Laura',  'Martinez', 'laura.martinez@uda.ar', '2018-01-10', 'FULL_TIME',   90000.00, 14, true),
    (4, 'Diego',  'Lopez',    'diego.lopez@uda.ar',   '2022-11-20', 'CONTRACTOR',  60000.00, 14, true);
