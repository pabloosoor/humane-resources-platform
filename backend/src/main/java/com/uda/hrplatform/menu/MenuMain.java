package com.uda.hrplatform.menu;

import com.uda.hrplatform.AppConfig;

import java.util.Scanner;

// Punto de entrada del menu de terminal.
// Ejecutar con: ./gradlew :backend:runMenu
public class MenuMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppConfig config = new AppConfig();
        new MenuNavigator(scanner, config).start();
        scanner.close();
    }
}
