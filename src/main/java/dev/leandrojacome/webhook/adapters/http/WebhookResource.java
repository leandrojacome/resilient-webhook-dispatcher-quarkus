package dev.leandrojacome.webhook.adapters.http;

import dev.leandrojacome.webhook.application.DispatchDueWebhooks;
import dev.leandrojacome.webhook.application.QueueWebhook;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/webhooks")
public class WebhookResource {
    private final QueueWebhook queue;
    private final DispatchDueWebhooks dispatch;
    public WebhookResource(QueueWebhook queue, DispatchDueWebhooks dispatch) { this.queue = queue; this.dispatch = dispatch; }
    @POST public Response queue(@Valid Request request) {
        var job = queue.execute(URI.create(request.destination()), request.payload());
        return Response.accepted(new Queued(job.id().toString())).build();
    }
    @POST @Path("/dispatch") public DispatchResult dispatch() { return new DispatchResult(dispatch.execute()); }
    public record Request(@NotBlank String destination, @NotBlank String payload) {}
    public record Queued(String id) {}
    public record DispatchResult(int processed) {}
}
