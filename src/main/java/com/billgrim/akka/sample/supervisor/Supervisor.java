package com.billgrim.akka.sample.supervisor;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by billgrim on 2017/8/9.
 */
public class Supervisor extends AbstractLoggingActor {
    public static final OneForOneStrategy STRATEGY = new OneForOneStrategy(
            10,
            Duration.create("10 seconds"),
            DeciderBuilder
                    .match(RuntimeException.class, ex -> restart())
                    .build()
    );

    {
        final ActorRef child = getContext().actorOf(NonTrustWorthyChild.props());
        receive(ReceiveBuilder
                .matchAny(any -> child.forward(any, getContext()))
                .build()
        );
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return STRATEGY;
    }

    public static Props props() {
        return Props.create(Supervisor.class);
    }
}
