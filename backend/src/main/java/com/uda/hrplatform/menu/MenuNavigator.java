package com.uda.hrplatform.menu;

import com.uda.hrplatform.AppConfig;

import java.util.Scanner;

// Menu principal. Muestra las opciones de navegacion y delega a cada sub-menu.
// Cada opcion del switch lleva a un dominio distinto del sistema.
public class MenuNavigator {

    private final Scanner scanner;
    private final EmployeeMenu employeeMenu;
    private final VacationMenu vacationMenu;
    private final BonusMenu bonusMenu;
    private final AttendanceMenu attendanceMenu;

    public MenuNavigator(Scanner scanner, AppConfig config) {
        this.scanner        = scanner;
        this.employeeMenu   = new EmployeeMenu(scanner, config.getEmployeeService());
        this.vacationMenu   = new VacationMenu(scanner, config.getVacationService());
        this.bonusMenu      = new BonusMenu(scanner, config.getBonusService());
        this.attendanceMenu = new AttendanceMenu(scanner, config.getAttendanceBonusService());
    }

    public void start() {
        System.out.println("==========================================");
        System.out.println("      HR Platform - Menu de Terminal      ");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Empleados");
            System.out.println("2. Vacaciones");
            System.out.println("3. Bonos");
            System.out.println("4. Asistencia");
            System.out.println("0. Salir");
            System.out.print("\n> ");

            switch (scanner.nextLine().trim()) {
                case "1" -> employeeMenu.show();
                case "2" -> vacationMenu.show();
                case "3" -> bonusMenu.show();
                case "4" -> attendanceMenu.show();
                case "0" -> running = false;
                default  -> System.out.println("Opcion invalida. Intenta de nuevo.");
            }
        }

        System.out.println("\nSaliendo del sistema. Hasta luego.");
    }
}
