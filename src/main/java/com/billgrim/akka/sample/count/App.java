package com.billgrim.akka.sample.count;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by billgrim on 2017/8/9.
 */
public class App {
    static class Counter extends AbstractLoggingActor {
        //protocol
        static class Message {
        }

        private int counter = 0;

        private void onMessage(Message message) {
            counter++;
            log().info("Increased counter " + counter);
        }

        {
            receive(ReceiveBuilder.match(Message.class, this::onMessage).build());
        }

        static public Props props() {
            return Props.create(Counter.class);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("counterSystem");
        ActorRef counter = system.actorOf(Counter.props(), "counter");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                counter.tell(new Counter.Message(), ActorRef.noSender());
            }
        }
        System.out.println("Enter to terminate");
//        String s = StdIn.readLine();
    }
}
