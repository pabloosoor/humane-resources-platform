package com.uda.hrplatform.menu;

import com.uda.hrplatform.dto.CalculateBonusRequest;
import com.uda.hrplatform.model.BonusRecord;
import com.uda.hrplatform.model.BonusType;
import com.uda.hrplatform.service.BonusService;

import java.util.List;
import java.util.Scanner;

// Menu de bonos.
// Aqui se puede ver el OCP aplicado en BonusCalculator: el servicio
// busca la calculadora correcta segun el tipo de bono pedido.
// Agregar un nuevo tipo de bono no requiere modificar este menu ni el servicio.
public class BonusMenu {

    private final Scanner scanner;
    private final BonusService service;

    public BonusMenu(Scanner scanner, BonusService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Bonos ---");
            System.out.println("1. Calcular bono de antiguedad (SENIORITY)");
            System.out.println("2. Ver historial de bonos");
            System.out.println("0. Volver");
            System.out.print("\n> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> calcular();
                case "2" -> verHistorial();
                case "0" -> back = true;
                default  -> System.out.println("Opcion invalida.");
            }
        }
    }

    // Calcula el bono de antiguedad: 1% del salario base por cada anio trabajado.
    // Falla si ya se calculo el mismo tipo de bono para ese periodo.
    private void calcular() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Periodo (YYYY-MM, ej: 2025-06): ");
            String periodo = scanner.nextLine().trim();

            BonusRecord registro = service.calculate(id,
                    new CalculateBonusRequest(BonusType.SENIORITY, periodo));

            System.out.printf("Bono calculado: $%.2f | Tipo: %s | Periodo: %s%n",
                    registro.getAmount(), registro.getBonusType(), registro.getPeriod());
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Muestra el historial de todos los bonos calculados para un empleado.
    private void verHistorial() {
        System.out.print("ID del empleado: ");
        try {
            long id = Long.parseLong(scanner.nextLine().trim());
            List<BonusRecord> lista = service.findByEmployee(id);

            if (lista.isEmpty()) {
                System.out.println("No hay bonos registrados para este empleado.");
                return;
            }

            System.out.println();
            System.out.printf("%-4s | %-20s | %-12s | %s%n",
                    "ID", "Tipo", "Monto", "Periodo");
            System.out.println("-".repeat(52));
            for (BonusRecord b : lista) {
                System.out.printf("%-4d | %-20s | $%-11.2f | %s%n",
                        b.getId(), b.getBonusType(), b.getAmount(), b.getPeriod());
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID debe ser un numero.");
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
