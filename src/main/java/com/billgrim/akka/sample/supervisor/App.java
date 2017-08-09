package com.billgrim.akka.sample.supervisor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.io.StdIn;

/**
 * Created by billgrim on 2017/8/9.
 */
public class App {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef supervisor = system.actorOf(Supervisor.props(), "supervisor");

        for (int i =0;i<10;i++){
            supervisor.tell(new NonTrustWorthyChild.Command(),ActorRef.noSender());
        }
        System.out.println("Enter to terminate");
//        StdIn.readLine();
        system.terminate();
    }
}
