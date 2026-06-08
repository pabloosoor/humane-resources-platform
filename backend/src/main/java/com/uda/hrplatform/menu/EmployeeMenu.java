package com.uda.hrplatform.menu;

import com.uda.hrplatform.dto.CreateEmployeeRequest;
import com.uda.hrplatform.dto.UpdateEmployeeRequest;
import com.uda.hrplatform.model.Employee;
import com.uda.hrplatform.model.EmployeeType;
import com.uda.hrplatform.service.EmployeeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// Menu de gestion de empleados.
// Cada metodo privado corresponde a una operacion del CRUD.
// Usa EmployeeService directamente, igual que el EmployeeController de la API REST.
public class EmployeeMenu {

    private final Scanner scanner;
    private final EmployeeService service;

    public EmployeeMenu(Scanner scanner, EmployeeService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Empleados ---");
            System.out.println("1. Listar empleados activos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Crear empleado");
            System.out.println("4. Actualizar salario");
            System.out.println("5. Dar de baja");
            System.out.println("0. Volver");
            System.out.print("\n> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> listar();
                case "2" -> buscarPorId();
                case "3" -> crear();
                case "4" -> actualizar();
                case "5" -> darDeBaja();
                case "0" -> back = true;
                default  -> System.out.println("Opcion invalida.");
            }
        }
    }

    // Muestra todos los empleados con active = true en formato de tabla.
    private void listar() {
        List<Employee> empleados = service.findActives();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados activos registrados.");
            return;
        }
        System.out.println();
        System.out.printf("%-4s | %-22s | %-14s | %-12s | %s%n",
                "ID", "Nombre", "Tipo", "Salario", "Vacaciones");
        System.out.println("-".repeat(72));
        for (Employee e : empleados) {
            System.out.printf("%-4d | %-22s | %-14s | %-12.2f | %d dias%n",
                    e.getId(),
                    e.getFirstName() + " " + e.getLastName(),
                    e.getEmployeeType(),
                    e.getBaseSalary(),
                    e.getVacationDays());
        }
    }

    // Busca un empleado por ID. Muestra error si no existe.
    private void buscarPorId() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            Employee e = service.findById(id);
            System.out.println("\n" + formatearEmpleado(e));
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Solicita todos los campos necesarios y crea el empleado con 14 dias de vacaciones por defecto.
    private void crear() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Fecha de ingreso (YYYY-MM-DD): ");
            LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Tipo de contratacion:");
            System.out.println("  1. FULL_TIME");
            System.out.println("  2. PART_TIME");
            System.out.println("  3. CONTRACTOR");
            System.out.print("> ");
            EmployeeType tipo = switch (scanner.nextLine().trim()) {
                case "1" -> EmployeeType.FULL_TIME;
                case "2" -> EmployeeType.PART_TIME;
                case "3" -> EmployeeType.CONTRACTOR;
                default  -> throw new RuntimeException("Tipo invalido.");
            };

            System.out.print("Salario base: ");
            BigDecimal salario = new BigDecimal(scanner.nextLine().trim());

            Employee creado = service.create(
                    new CreateEmployeeRequest(nombre, apellido, email, fechaIngreso, tipo, salario));

            System.out.println("Empleado creado con ID: " + creado.getId());
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Actualiza solo el salario. Los campos null se ignoran en el servicio (patch parcial).
    private void actualizar() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Nuevo salario (Enter para no cambiar): ");
            String input = scanner.nextLine().trim();
            BigDecimal salario = input.isEmpty() ? null : new BigDecimal(input);

            Employee actualizado = service.update(id,
                    new UpdateEmployeeRequest(null, null, null, salario, null));
            System.out.println("Actualizado: " + formatearEmpleado(actualizado));
        } catch (NumberFormatException ex) {
            System.out.println("Error: valor numerico invalido.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Baja logica: pone active = false. El empleado no se borra de la BD.
    private void darDeBaja() {
        System.out.print("ID del empleado a dar de baja: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            service.deactivate(id);
            System.out.println("Empleado dado de baja correctamente.");
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private String formatearEmpleado(Employee e) {
        return String.format("ID: %d | %s %s | %s | Salario: $%.2f | Vacaciones: %d dias | Activo: %s",
                e.getId(), e.getFirstName(), e.getLastName(),
                e.getEmployeeType(), e.getBaseSalary(),
                e.getVacationDays(), e.isActive() ? "Si" : "No");
    }
}
