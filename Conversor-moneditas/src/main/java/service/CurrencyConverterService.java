package service;

import model.Currency;
import model.ExchangeRateResponse;

import java.io.IOException;
import java.util.Map;

public class CurrencyConverterService {
    private final ExchangeRateService exchangeRateService;
    private ExchangeRateResponse cachedRates;

    public CurrencyConverterService() {
        this.exchangeRateService = new ExchangeRateService();
    }

    public void loadExchangeRates() throws IOException, InterruptedException {
        System.out.println("Cargando tipos de cambio...");
        this.cachedRates = exchangeRateService.getExchangeRates();

        if (!cachedRates.isSuccessful()) {
            throw new IOException("No se pudieron obtener los tipos de cambio");
        }
        System.out.println("Tipos de cambio cargados exitosamente.");
    }

    public double convertCurrency(Currency from, Currency to, double amount) {
        if (cachedRates == null || !cachedRates.isSuccessful()) {
            throw new IllegalStateException("Los tipos de cambio no están disponibles");
        }

        Map<String, Double> rates = cachedRates.getConversion_rates();

        // Si convertimos desde USD
        if (from == Currency.USD) {
            Double rate = rates.get(to.getCode());
            if (rate == null) {
                throw new IllegalArgumentException("Moneda no encontrada: " + to.getCode());
            }
            return amount * rate;
        }

        // Si convertimos hacia USD
        if (to == Currency.USD) {
            Double rate = rates.get(from.getCode());
            if (rate == null) {
                throw new IllegalArgumentException("Moneda no encontrada: " + from.getCode());
            }
            return amount / rate;
        }

        // Para otras conversiones (no debería ocurrir con el menú actual)
        throw new UnsupportedOperationException("Conversión no soportada: " +
                from.getCode() + " a " + to.getCode());
    }

    public double getExchangeRate(Currency from, Currency to) {
        return convertCurrency(from, to, 1.0);
    }
}