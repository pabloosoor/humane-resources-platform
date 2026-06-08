package com.uda.hrplatform.menu;

import com.uda.hrplatform.dto.NewVacationRequest;
import com.uda.hrplatform.model.VacationRequest;
import com.uda.hrplatform.service.VacationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// Menu de vacaciones.
// Aqui se puede ver en accion el OCP: la politica de vacaciones
// se aplica automaticamente segun el tipo del empleado. Si se aprueba,
// se descuentan los dias del saldo. Si no, se guarda como REJECTED.
public class VacationMenu {

    private final Scanner scanner;
    private final VacationService service;

    public VacationMenu(Scanner scanner, VacationService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Vacaciones ---");
            System.out.println("1. Solicitar vacaciones");
            System.out.println("2. Ver historial de vacaciones");
            System.out.println("0. Volver");
            System.out.print("\n> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> solicitar();
                case "2" -> verHistorial();
                case "0" -> back = true;
                default  -> System.out.println("Opcion invalida.");
            }
        }
    }

    // Solicita vacaciones para un empleado.
    // La VacationPolicy decide si se aprueba o rechaza segun el saldo disponible.
    private void solicitar() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Fecha de inicio (YYYY-MM-DD): ");
            LocalDate inicio = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Fecha de fin (YYYY-MM-DD): ");
            LocalDate fin = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Dias solicitados: ");
            int dias = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Motivo: ");
            String motivo = scanner.nextLine().trim();

            VacationRequest resultado = service.request(id,
                    new NewVacationRequest(inicio, fin, dias, motivo));

            System.out.println("Solicitud registrada.");
            System.out.println("Estado: " + resultado.getStatus());
            System.out.println("ID de solicitud: " + resultado.getId());
        } catch (NumberFormatException ex) {
            System.out.println("Error: valor numerico invalido.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Muestra todas las solicitudes de vacaciones de un empleado (aprobadas, rechazadas, etc.).
    private void verHistorial() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            List<VacationRequest> lista = service.findByEmployee(id);

            if (lista.isEmpty()) {
                System.out.println("No hay solicitudes de vacaciones registradas.");
                return;
            }

            System.out.println();
            System.out.printf("%-4s | %-12s | %-12s | %-5s | %-10s | %s%n",
                    "ID", "Inicio", "Fin", "Dias", "Estado", "Motivo");
            System.out.println("-".repeat(72));
            for (VacationRequest v : lista) {
                System.out.printf("%-4d | %-12s | %-12s | %-5d | %-10s | %s%n",
                        v.getId(), v.getStartDate(), v.getEndDate(),
                        v.getRequestedDays(), v.getStatus(), v.getReason());
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
