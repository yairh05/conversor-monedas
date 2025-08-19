package view;

import model.Currency;
import service.CurrencyConverterService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuView {
    private final Scanner scanner;
    private final CurrencyConverterService converterService;

    public MenuView() {
        this.scanner = new Scanner(System.in);
        this.converterService = new CurrencyConverterService();
    }

    public void start() {
        try {
            converterService.loadExchangeRates();
            showWelcomeMessage();
            runMainLoop();
        } catch (Exception e) {
            System.out.println("Error al inicializar la aplicación: " + e.getMessage());
        }
    }

    private void showWelcomeMessage() {
        System.out.println("=================================");
        System.out.println("  CONVERSOR DE DIVISAS");
        System.out.println("=================================");
    }

    private void runMainLoop() {
        boolean running = true;

        while (running) {
            showMenu();
            int option = getUserOption();

            if (option == 7) {
                running = false;
                showGoodbyeMessage();
            } else if (isValidOption(option)) {
                processConversion(option);
            } else {
                showInvalidOptionMessage();
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== MENÚ DE CONVERSIONES ===");
        System.out.println("1) Dolar =>> Peso argentino");
        System.out.println("2) Peso argentino =>> Dolar");
        System.out.println("3) Dolar =>> Real brasileño");
        System.out.println("4) Real brasileño =>> Dolar");
        System.out.println("5) Dolar =>> Peso colombiano");
        System.out.println("6) Peso colombiano =>> Dolar");
        System.out.println("7) Salir");
        System.out.print("Elija una opción válida: ");
    }

    private int getUserOption() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    private boolean isValidOption(int option) {
        return option >= 1 && option <= 6;
    }

    private void processConversion(int option) {
        ConversionPair pair = getConversionPair(option);

        System.out.printf("\n=== Conversión: %s a %s ===\n",
                pair.from.getDisplayName(), pair.to.getDisplayName());

        double amount = getAmountFromUser(pair.from);

        try {
            double convertedAmount = converterService.convertCurrency(pair.from, pair.to, amount);
            double exchangeRate = converterService.getExchangeRate(pair.from, pair.to);

            showConversionResult(pair, amount, convertedAmount, exchangeRate);
        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e.getMessage());
        }
    }

    private ConversionPair getConversionPair(int option) {
        return switch (option) {
            case 1 -> new ConversionPair(Currency.USD, Currency.ARS);
            case 2 -> new ConversionPair(Currency.ARS, Currency.USD);
            case 3 -> new ConversionPair(Currency.USD, Currency.BRL);
            case 4 -> new ConversionPair(Currency.BRL, Currency.USD);
            case 5 -> new ConversionPair(Currency.USD, Currency.COP);
            case 6 -> new ConversionPair(Currency.COP, Currency.USD);
            default -> throw new IllegalArgumentException("Opción inválida: " + option);
        };
    }

    private double getAmountFromUser(Currency currency) {
        while (true) {
            try {
                System.out.printf("Ingrese la cantidad de %s a convertir: ", currency.getDisplayName());
                double amount = scanner.nextDouble();

                if (amount < 0) {
                    System.out.println("La cantidad debe ser positiva.");
                    continue;
                }

                return amount;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }

    private void showConversionResult(ConversionPair pair, double amount,
                                      double convertedAmount, double exchangeRate) {
        System.out.println("\n--- RESULTADO ---");
        System.out.printf("%.2f %s = %.2f %s\n",
                amount, pair.from.getDisplayName(),
                convertedAmount, pair.to.getDisplayName());
        System.out.printf("Tasa de cambio: 1 %s = %.4f %s\n",
                pair.from.getDisplayName(), exchangeRate, pair.to.getDisplayName());
    }

    private void showInvalidOptionMessage() {
        System.out.println("\nElija una opción válida");
    }

    private void showGoodbyeMessage() {
        System.out.println("\n¡Gracias por usar el Conversor de Divisas!");
        System.out.println("¡Hasta luego!");
    }

    // Clase interna para manejar pares de conversión
    private static class ConversionPair {
        final Currency from;
        final Currency to;

        ConversionPair(Currency from, Currency to) {
            this.from = from;
            this.to = to;
        }
    }
}
