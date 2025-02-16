package ke.co.velnova.springstatemachine.service;

import jakarta.annotation.PostConstruct;
import ke.co.velnova.springstatemachine.config.OrderEvents;
import ke.co.velnova.springstatemachine.config.OrderStates;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final StateMachine<OrderStates, OrderEvents> stateMachine;

    public OrderService(StateMachine<OrderStates, OrderEvents> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void startPayment() {
        stateMachine.sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvents.START_PAYMENT)
                        .build()))
                .subscribe(result -> System.out.println("startPayment = " + result));
    }

    public void confirmPayment() {
        stateMachine.sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvents.PAYMENT_SUCCESS)
                        .build()))
                .subscribe(result -> System.out.println("confirmPayment = " + result));
    }

    public void failPayment() {
        stateMachine.sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvents.PAYMENT_FAILED)
                        .build()))
                .subscribe(result -> System.out.println("failPayment = " + result));
    }

    public void checkStock() {
        stateMachine.sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvents.CHECK_STOCK)
                        .build()))
                .subscribe(result -> System.out.println("checkStock = " + result));
    }

    public void shipOrder() {
        stateMachine.sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvents.SHIP_ORDER)
                        .build()))
                .subscribe(result -> System.out.println("shipOrder = " + result));
    }

    @PostConstruct
    public void init(){
        stateMachine.startReactively().subscribe();
        startPayment();
    }
}