package service;

import com.google.gson.Gson;
import model.ExchangeRateResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExchangeRateService {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/0e6eb01822a61f024bb23273/latest/USD";
    private final HttpClient httpClient;
    private final Gson gson;

    public ExchangeRateService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public ExchangeRateResponse getExchangeRates() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), ExchangeRateResponse.class);
        } else {
            throw new IOException("Error en la API: " + response.statusCode());
        }
    }
}
