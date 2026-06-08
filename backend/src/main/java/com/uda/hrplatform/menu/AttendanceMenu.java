package com.uda.hrplatform.menu;

import com.uda.hrplatform.dto.RegisterAttendanceRequest;
import com.uda.hrplatform.model.AttendanceRecord;
import com.uda.hrplatform.model.BonusRecord;
import com.uda.hrplatform.service.AttendanceBonusService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// Menu de asistencia y bono de presentismo.
// El registro de asistencia es por dia (no se puede duplicar el mismo dia).
// El bono de presentismo se calcula al cierre del mes: 10% del salario
// si no hay ausencias injustificadas en el periodo; cero si las hay.
public class AttendanceMenu {

    private final Scanner scanner;
    private final AttendanceBonusService service;

    public AttendanceMenu(Scanner scanner, AttendanceBonusService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Asistencia ---");
            System.out.println("1. Registrar asistencia");
            System.out.println("2. Ver asistencias por periodo");
            System.out.println("3. Calcular bono de presentismo");
            System.out.println("0. Volver");
            System.out.print("\n> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> registrar();
                case "2" -> verPorPeriodo();
                case "3" -> calcularPresentismo();
                case "0" -> back = true;
                default  -> System.out.println("Opcion invalida.");
            }
        }
    }

    // Registra la asistencia de un dia para un empleado.
    // Si el empleado estuvo ausente, pregunta si la ausencia fue justificada.
    private void registrar() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Fecha (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Estuvo presente? (s/n): ");
            boolean presente = scanner.nextLine().trim().equalsIgnoreCase("s");

            boolean justificada = false;
            if (!presente) {
                System.out.print("La ausencia fue justificada? (s/n): ");
                justificada = scanner.nextLine().trim().equalsIgnoreCase("s");
            }

            AttendanceRecord registro = service.register(id,
                    new RegisterAttendanceRequest(fecha, presente, justificada));

            System.out.println("Asistencia registrada. ID: " + registro.getId());
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Muestra todos los registros de asistencia de un empleado en un mes.
    private void verPorPeriodo() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Periodo (YYYY-MM, ej: 2025-06): ");
            String periodo = scanner.nextLine().trim();

            List<AttendanceRecord> lista = service.findByEmployeeAndPeriod(id, periodo);

            if (lista.isEmpty()) {
                System.out.println("No hay registros para ese periodo.");
                return;
            }

            System.out.println();
            System.out.printf("%-4s | %-12s | %-10s | %s%n",
                    "ID", "Fecha", "Presente", "Justificada");
            System.out.println("-".repeat(44));
            for (AttendanceRecord r : lista) {
                System.out.printf("%-4d | %-12s | %-10s | %s%n",
                        r.getId(), r.getDate(),
                        r.isPresent() ? "Si" : "No",
                        r.isJustifiedAbsence() ? "Si" : "No");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Calcula el bono de presentismo del mes indicado.
    // Usa StandardAttendanceBonusCalculator (OCP): 10% si sin ausencias injustificadas, 0 si las hay.
    private void calcularPresentismo() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Periodo (YYYY-MM, ej: 2025-06): ");
            String periodo = scanner.nextLine().trim();

            BonusRecord bono = service.calculateBonus(id, periodo);

            System.out.printf("Bono de presentismo: $%.2f | Periodo: %s%n",
                    bono.getAmount(), bono.getPeriod());
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
