package dev.leandrojacome.webhook.adapters;

import dev.leandrojacome.webhook.application.ports.DeliveryClient;
import dev.leandrojacome.webhook.domain.WebhookJob;
import jakarta.enterprise.context.ApplicationScoped;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class JdkHttpDeliveryClient implements DeliveryClient {
    private final HttpClient client = HttpClient.newHttpClient();
    public boolean deliver(WebhookJob job) {
        try {
            var request = HttpRequest.newBuilder(job.destination()).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(job.payload())).build();
            int status = client.send(request, HttpResponse.BodyHandlers.discarding()).statusCode();
            return status >= 200 && status < 300;
        } catch (Exception ignored) {
            return false;
        }
    }
}
