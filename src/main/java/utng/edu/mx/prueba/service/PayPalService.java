package utng.edu.mx.prueba.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;
import org.json.JSONArray;

import utng.edu.mx.prueba.model.PayPalTransaction;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Base64;

@Service
public class PayPalService {

    private static final Logger logger = LoggerFactory.getLogger(PayPalService.class);

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.api.base-url}")
    private String paypalApiBaseUrl;

    private String getAccessToken() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(paypalApiBaseUrl + "/v1/oauth2/token");

            // Configurar credenciales
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // Establecer el cuerpo de la solicitud
            StringEntity entity = new StringEntity("grant_type=client_credentials");
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.info("Access Token Response: {}", responseBody);
                JSONObject responseJson = new JSONObject(responseBody);
                return responseJson.getString("access_token");
            }
        }
    }

    public PayPalTransaction createPayPalTransaction(double amount, String currency) {
        try {
            String accessToken = getAccessToken();

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(paypalApiBaseUrl + "/v2/checkout/orders");

                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + accessToken);

                // Usar la API v2 de PayPal para crear órdenes
                JSONObject paymentJson = new JSONObject();
                paymentJson.put("intent", "CAPTURE");
                paymentJson.put("purchase_units", new org.json.JSONArray()
                        .put(new JSONObject()
                                .put("amount", new JSONObject()
                                        .put("currency_code", currency)
                                        .put("value", String.format("%.2f", amount))
                                )
                        )
                );

                StringEntity entity = new StringEntity(paymentJson.toString());
                httpPost.setEntity(entity);

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    logger.info("Create Transaction Response: {}", responseBody);

                    JSONObject responseJson = new JSONObject(responseBody);

                    PayPalTransaction transaction = new PayPalTransaction();
                    transaction.setTransactionId(responseJson.getString("id"));
                    transaction.setAmount(amount);
                    transaction.setCurrency(currency);
                    transaction.setStatus(responseJson.getString("status"));

                    return transaction;
                }
            }
        } catch (Exception e) {
            logger.error("Error creating PayPal transaction", e);
            return null;
        }
    }

    public List<PayPalTransaction> listPayPalTransactions() {
        List<PayPalTransaction> transactions = new ArrayList<>();

        try {
            String accessToken = getAccessToken();

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                // Construir URL con parámetros para filtrar transacciones
                String startDate = ZonedDateTime.now(ZoneOffset.UTC)
                        .minusDays(30)
                        .format(DateTimeFormatter.ISO_INSTANT);

                String url = paypalApiBaseUrl + "/v1/reporting/transactions" +
                        "?start_date=" + URLEncoder.encode(startDate, StandardCharsets.UTF_8) +
                        "&end_date=" + URLEncoder.encode(ZonedDateTime.now(ZoneOffset.UTC)
                                .format(DateTimeFormatter.ISO_INSTANT),
                        StandardCharsets.UTF_8) +
                        "&page_size=100" +  // Ajusta según tus necesidades
                        "&page=1";

                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + accessToken);

                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != 200) {
                        logger.error("Error al obtener transacciones. Código de estado: {}", statusCode);
                        return transactions;
                    }

                    String responseBody = EntityUtils.toString(response.getEntity());
                    logger.debug("Respuesta de transacciones PayPal: {}", responseBody);

                    // Parsear la respuesta JSON
                    JSONObject responseJson = new JSONObject(responseBody);

                    // Verificar la estructura de la respuesta
                    if (responseJson.has("transaction_details")) {
                        JSONArray transactionsArray = responseJson.getJSONArray("transaction_details");

                        for (int i = 0; i < transactionsArray.length(); i++) {
                            JSONObject transactionJson = transactionsArray.getJSONObject(i);

                            PayPalTransaction transaction = new PayPalTransaction();

                            // Extraer detalles de la transacción
                            if (transactionJson.has("transaction_info")) {
                                JSONObject transactionInfo = transactionJson.getJSONObject("transaction_info");

                                transaction.setTransactionId(transactionInfo.optString("transaction_id"));
                                transaction.setStatus(transactionInfo.optString("transaction_status"));

                                // Manejar el monto
                                if (transactionInfo.has("transaction_amount")) {
                                    JSONObject amountJson = transactionInfo.getJSONObject("transaction_amount");
                                    transaction.setAmount(amountJson.optDouble("value", 0.0));
                                    transaction.setCurrency(amountJson.optString("currency"));
                                }

                                // Fecha de creación
                                String transactionDate = transactionInfo.optString("transaction_initiation_date");
                                if (transactionDate != null && !transactionDate.isEmpty()) {
                                    try {
                                        transaction.setCreatedAt(Date.from(Instant.parse(transactionDate)));
                                    } catch (Exception e) {
                                        logger.warn("No se pudo parsear la fecha: {}", transactionDate);
                                    }
                                }
                            }

                            transactions.add(transaction);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error al listar transacciones de PayPal", e);
        }

        return transactions;
    }

    public PayPalTransaction getTransactionDetails(String transactionId) {
        try {
            String accessToken = getAccessToken();

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(paypalApiBaseUrl + "/v2/checkout/orders/" + transactionId);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + accessToken);

                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    // Verificar el código de estado
                    if (response.getStatusLine().getStatusCode() != 200) {
                        logger.error("Error response from PayPal: {}", response.getStatusLine());
                        return null;
                    }

                    String responseBody = EntityUtils.toString(response.getEntity());
                    logger.debug("Raw PayPal response: {}", responseBody);

                    // Verificar si la respuesta es válida
                    if (responseBody == null || responseBody.trim().isEmpty() || !responseBody.trim().startsWith("{")) {
                        logger.error("Invalid JSON response from PayPal: {}", responseBody);
                        return null;
                    }

                    JSONObject responseJson = new JSONObject(responseBody);

                    PayPalTransaction transaction = new PayPalTransaction();
                    transaction.setTransactionId(responseJson.getString("id"));
                    transaction.setStatus(responseJson.getString("status"));

                    if (responseJson.has("purchase_units") && responseJson.getJSONArray("purchase_units").length() > 0) {
                        JSONObject amountJson = responseJson.getJSONArray("purchase_units")
                                .getJSONObject(0)
                                .getJSONObject("amount");

                        transaction.setAmount(amountJson.getDouble("value"));
                        transaction.setCurrency(amountJson.getString("currency_code"));
                    }

                    return transaction;
                }
            }
        } catch (Exception e) {
            logger.error("Error getting transaction details", e);
            return null;
        }
    }
}
