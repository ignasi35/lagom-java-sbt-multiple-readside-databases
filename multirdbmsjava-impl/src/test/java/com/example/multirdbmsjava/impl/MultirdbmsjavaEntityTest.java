package com.example.multirdbmsjava.impl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import com.example.multirdbmsjava.impl.MultirdbmsjavaCommand.Hello;
import com.example.multirdbmsjava.impl.MultirdbmsjavaCommand.UseGreetingMessage;
import com.example.multirdbmsjava.impl.MultirdbmsjavaEvent.GreetingMessageChanged;

import static org.junit.Assert.assertEquals;

public class MultirdbmsjavaEntityTest {
    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("MultirdbmsjavaEntityTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testMultirdbmsjavaEntity() {
        PersistentEntityTestDriver<MultirdbmsjavaCommand, MultirdbmsjavaEvent, MultirdbmsjavaState> driver = new PersistentEntityTestDriver<>(system,
                new MultirdbmsjavaEntity(), "world-1");

        Outcome<MultirdbmsjavaEvent, MultirdbmsjavaState> outcome1 = driver.run(new Hello("Alice"));
        assertEquals("Hello, Alice!", outcome1.getReplies().get(0));
        assertEquals(Collections.emptyList(), outcome1.issues());

        Outcome<MultirdbmsjavaEvent, MultirdbmsjavaState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
                new Hello("Bob"));
        assertEquals(1, outcome2.events().size());
        assertEquals(new GreetingMessageChanged("world-1", "Hi"), outcome2.events().get(0));
        assertEquals("Hi", outcome2.state().message);
        assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
        assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
        assertEquals(2, outcome2.getReplies().size());
        assertEquals(Collections.emptyList(), outcome2.issues());
    }
}
