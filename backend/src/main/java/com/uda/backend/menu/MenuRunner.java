package com.uda.backend.menu;

import com.uda.accessdata.bonus.BonusRecord;
import com.uda.accessdata.bonus.BonusResult;
import com.uda.accessdata.bonus.BonusType;
import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.presentismo.PresentismoResult;
import com.uda.accessdata.vacation.VacationRequest;
import com.uda.accessdata.vacation.VacationResult;
import com.uda.backend.bonus.BonusService;
import com.uda.backend.employee.EmployeeService;
import com.uda.backend.presentismo.PresentismoService;
import com.uda.backend.vacation.VacationService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuRunner {

    private final EmployeeService employeeService;
    private final VacationService vacationService;
    private final BonusService bonusService;
    private final PresentismoService presentismoService;
    private final Scanner scanner = new Scanner(System.in);

    public MenuRunner(EmployeeService employeeService,
                      VacationService vacationService,
                      BonusService bonusService,
                      PresentismoService presentismoService) {
        this.employeeService = employeeService;
        this.vacationService = vacationService;
        this.bonusService = bonusService;
        this.presentismoService = presentismoService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenuPrincipal();
            int opcion = readInt();
            switch (opcion) {
                case 1 -> menuVacaciones();
                case 2 -> menuBonos();
                case 3 -> menuPresentismo();
                case 4 -> listarEmpleados();
                case 0 -> running = false;
                default -> System.out.println("  Opción inválida.\n");
            }
        }
        System.out.println("\n  Hasta luego.\n");
    }

    private void menuVacaciones() {
        System.out.println("\n── VACACIONES ─────────────────────────────");
        listarEmpleados();
        try {
            Long id = readLong("  ID del empleado : ");
            int dias = readPositiveInt("  Días a solicitar: ");
            LocalDate inicio = readFecha("  Fecha de inicio  (YYYY-MM-DD): ");

            VacationResult result = vacationService.solicitarVacaciones(id, dias, inicio);

            System.out.println("\n  Estado  : " + (result.aprobada() ? " APROBADA" : " RECHAZADA"));
            System.out.println("  Mensaje : " + result.mensaje());
            System.out.println("  Saldo   : " + result.diasRestantes() + " día(s) restante(s)");
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }

    private void menuBonos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n── BONOS ──────────────────────────────────");
            System.out.println("  1. Bono por Antigüedad");
            System.out.println("  2. Ver historial de bonos");
            System.out.println("  0. Volver");
            System.out.print("  Opción: ");
            switch (readInt()) {
                case 1 -> calcularBonoAntiguedad();
                case 2 -> verHistorialBonos();
                case 0 -> volver = true;
                default -> System.out.println("  Opción inválida.");
            }
        }
    }

    private void calcularBonoAntiguedad() {
        listarEmpleados();
        try {
            Long id = readLong("  ID del empleado: ");
            String periodo = readPeriodo("  Período (YYYY-MM): ");

            BonusResult result = bonusService.calcular(id, BonusType.ANTIGUEDAD, periodo);

            System.out.println("\n  Detalle : " + result.descripcion());
            System.out.printf("  Monto   : $%,.2f%n", result.monto());
            System.out.println("  ✔ Bono registrado.");
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private void verHistorialBonos() {
        listarEmpleados();
        try {
            Long id = readLong("  ID del empleado: ");
            List<BonusRecord> historial = bonusService.historialPorEmpleado(id);
            if (historial.isEmpty()) {
                System.out.println("  Sin bonos registrados.");
                return;
            }
            System.out.println();
            for (BonusRecord b : historial) {
                System.out.printf("  [%s] %-15s → $%,.2f%n", b.getPeriodo(), b.getTipoBono(), b.getMonto());
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private void menuPresentismo() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n── PRESENTISMO ────────────────────────────");
            System.out.println("  1. Calcular presentismo");
            System.out.println("  2. Registrar asistencia");
            System.out.println("  0. Volver");
            System.out.print("  Opción: ");
            switch (readInt()) {
                case 1 -> calcularPresentismo();
                case 2 -> registrarAsistencia();
                case 0 -> volver = true;
                default -> System.out.println("  Opción inválida.");
            }
        }
    }

    private void calcularPresentismo() {
        listarEmpleados();
        try {
            Long id = readLong("  ID del empleado: ");
            String periodo = readPeriodo("  Período (YYYY-MM): ");
            String[] partes = periodo.split("-");

            PresentismoResult result = presentismoService.calcular(id,
                    Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));

            System.out.println("\n  Ausencias injustificadas : " + result.ausenciasInjustificadas());
            System.out.println("  Califica                 : " + (result.califica() ? "✔ SÍ" : "✘ NO"));
            System.out.println("  Detalle                  : " + result.descripcion());
            if (result.califica()) {
                System.out.printf("  Monto                    : $%,.2f%n", result.monto());
                System.out.println("  ✔ Bono registrado.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private void registrarAsistencia() {
        listarEmpleados();
        try {
            Long id = readLong("  ID del empleado: ");
            LocalDate fecha = readFecha("  Fecha (YYYY-MM-DD): ");
            System.out.print("  ¿Estuvo presente? (s/n): ");
            boolean presente = scanner.nextLine().trim().equalsIgnoreCase("s");
            boolean justificada = false;
            if (!presente) {
                System.out.print("  ¿Ausencia justificada? (s/n): ");
                justificada = scanner.nextLine().trim().equalsIgnoreCase("s");
            }
            presentismoService.registrarAsistencia(id, fecha, presente, justificada);
            System.out.println("  ✔ Asistencia registrada.");
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private void printMenuPrincipal() {
        System.out.println("\n========================================");
        System.out.println("         HR Platform — UDA");
        System.out.println("========================================");
        System.out.println("  1. Vacaciones");
        System.out.println("  2. Bonos");
        System.out.println("  3. Presentismo");
        System.out.println("  4. Ver empleados");
        System.out.println("  0. Salir");
        System.out.print("  Opción: ");
    }

    private void listarEmpleados() {
        List<Employee> empleados = employeeService.listarActivos();
        System.out.println();
        for (Employee e : empleados) {
            System.out.printf("  [%d] %s %s (%s) — %d días de vacaciones%n",
                    e.getId(), e.getNombre(), e.getApellido(),
                    e.getTipoEmpleado(), e.getDiasVacaciones());
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private Long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Long.parseLong(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  Ingrese un número válido."); }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v > 0) return v;
                System.out.println("  Debe ser mayor a 0.");
            } catch (NumberFormatException e) { System.out.println("  Ingrese un número válido."); }
        }
    }

    private LocalDate readFecha(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return LocalDate.parse(scanner.nextLine().trim()); }
            catch (DateTimeParseException e) { System.out.println("  Formato inválido. Use YYYY-MM-DD."); }
        }
    }

    private String readPeriodo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{4}-\\d{2}")) return input;
            System.out.println("  Formato inválido. Use YYYY-MM.");
        }
    }
}
