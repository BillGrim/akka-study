package com.billgrim.akka.sample.alarm;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by billgrim on 2017/8/9.
 */
public class App {
    static class Alarm extends AbstractLoggingActor {
        static class Activity {
        }

        static class Enable {
            private final String password;

            public Enable(String password) {
                this.password = password;
            }
        }

        private final String password;
        private final PartialFunction<Object, BoxedUnit> enabled;
        private final PartialFunction<Object, BoxedUnit> disabled;

        public Alarm(String password) {
            this.password = password;
            enabled = ReceiveBuilder.match(Activity.class,this::onActivity).match(Disable.class,this::onDisable).build();
            disabled = ReceiveBuilder.match(Enable.class,this::onEnable).build();
            receive(disabled);
        }

        private  void onEnable(Enable enable) {
            if(password.equals(enable.password)){
                log().info("Alarm enabled");
                getContext().become(enabled);
            }else {
                log().info("Someone failed to enable the alarm");
            }
        }

        private void onDisable(Disable disable) {
            if (password.equals(disable.password)){
                log().info("Alarm disabled");
                getContext().become(disabled);
            }else {
                log().warning("Someone who didn't know the password tried to disable the alarm");
            }
        }

        private void onActivity(Activity ignored) {
            log().warning("Alarm alarm!!!");
        }

        static class Disable {
            private final String password;

            public Disable(String password) {
                this.password = password;
            }
        }

        static Props props(String password) {
            return Props.create(Alarm.class, password);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("alarmSystem");
        final ActorRef alarm = system.actorOf(Alarm.props("cat"),"alarm");

        alarm.tell(new Alarm.Activity(),ActorRef.noSender());
        alarm.tell(new Alarm.Enable("dogs"),ActorRef.noSender());
        alarm.tell(new Alarm.Enable("cat"),ActorRef.noSender());
        alarm.tell(new Alarm.Activity(),ActorRef.noSender());
        alarm.tell(new Alarm.Disable("cow"),ActorRef.noSender());
        alarm.tell(new Alarm.Disable("cat"),ActorRef.noSender());
    }
}
