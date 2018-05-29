package com.example.multirdbmsjava.impl;

import akka.Done;
import akka.NotUsed;
import com.example.multirdbmsjava.api.GreetingMessage;
import com.example.multirdbmsjava.api.MultirdbmsjavaService;
import com.example.multirdbmsjava.impl.MultirdbmsjavaCommand.Hello;
import com.example.multirdbmsjava.impl.MultirdbmsjavaCommand.UseGreetingMessage;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

/**
 * Implementation of the MultirdbmsjavaService.
 */
public class MultirdbmsjavaServiceImpl implements MultirdbmsjavaService {
    private final PersistentEntityRegistry persistentEntityRegistry;
    private InventoryRepo inventoryRepo;

    @Inject
    public MultirdbmsjavaServiceImpl(PersistentEntityRegistry persistentEntityRegistry, InventoryRepo inventoryRepo) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.inventoryRepo = inventoryRepo;
        persistentEntityRegistry.register(MultirdbmsjavaEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, String> hello(String id) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<MultirdbmsjavaCommand> ref = persistentEntityRegistry.refFor(MultirdbmsjavaEntity.class, id);
            // Ask the entity the Hello command.
            return ref.ask(new Hello(id)).thenApply( (msg) -> {
                int count = inventoryRepo.count();
                return msg + "(" + count + " items in repo)";
            });
        };
    }

    @Override
    public ServiceCall<GreetingMessage, Done> useGreeting(String id) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<MultirdbmsjavaCommand> ref = persistentEntityRegistry.refFor(MultirdbmsjavaEntity.class, id);
            // Tell the entity to use the greeting message specified.
            return ref.ask(new UseGreetingMessage(request.message));
        };
    }

}
