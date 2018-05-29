package com.example.multirdbmsjava.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * The multirdbmsjava service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the MultirdbmsjavaService.
 */
public interface MultirdbmsjavaService extends Service {
    /**
     * Example:
     * curl http://localhost:9000/api/hello/Alice
     */
    ServiceCall<NotUsed, String> hello(String id);

    /**
     * Example:
     * curl -H "Content-Type: application/json" -X POST -d '{"message": "Hi"}' http://localhost:9000/api/hello/Alice
     */
    ServiceCall<GreetingMessage, Done> useGreeting(String id);

    @Override
    default Descriptor descriptor() {
        return named("multirdbmsjava")
                .withCalls(
                        pathCall("/api/hello/:id", this::hello),
                        pathCall("/api/hello/:id", this::useGreeting)
                )
                .withAutoAcl(true);
    }
}
