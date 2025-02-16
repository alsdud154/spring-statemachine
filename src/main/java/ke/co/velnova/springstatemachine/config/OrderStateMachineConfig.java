package ke.co.velnova.springstatemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@EnableStateMachine
@Configuration
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    private final JpaStateMachineRepository jpaStateMachineRepository;

    public OrderStateMachineConfig(JpaStateMachineRepository jpaStateMachineRepository) {
        this.jpaStateMachineRepository = jpaStateMachineRepository;
    }

    @Bean
    public StateMachineRuntimePersister<OrderStates, OrderEvents, String> stateMachineRuntimePersister() {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
        config
                .withPersistence()
                .runtimePersister(stateMachineRuntimePersister());
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStates.ORDER_CREATED)
                .state(OrderStates.PAYMENT_PENDING)
                .state(OrderStates.PAYMENT_SUCCESS)
                .state(OrderStates.STOCK_CHECKED)
                .state(OrderStates.SHIPPED)
                .end(OrderStates.ORDER_CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStates.ORDER_CREATED).target(OrderStates.PAYMENT_PENDING).event(OrderEvents.START_PAYMENT)
                .action(paymentAction())
                .and()
                .withExternal()
                .source(OrderStates.PAYMENT_PENDING).target(OrderStates.PAYMENT_SUCCESS).event(OrderEvents.PAYMENT_SUCCESS)
                .and()
                .withExternal()
                .source(OrderStates.PAYMENT_PENDING).target(OrderStates.ORDER_CANCELLED).event(OrderEvents.PAYMENT_FAILED)
                .action(cancelOrderAction())
                .and()
                .withExternal()
                .source(OrderStates.PAYMENT_SUCCESS).target(OrderStates.STOCK_CHECKED).event(OrderEvents.CHECK_STOCK)
                .action(stockCheckAction())
                .and()
                .withExternal()
                .source(OrderStates.STOCK_CHECKED).target(OrderStates.SHIPPED).event(OrderEvents.SHIP_ORDER)
                .action(shipOrderAction());
    }

    @Bean
    public Action<OrderStates, OrderEvents> paymentAction() {
        return context -> {
            // 결제 처리 로직
            System.out.println("결제 요청 처리...");
        };
    }

    @Bean
    public Action<OrderStates, OrderEvents> stockCheckAction() {
        return context -> {
            // 재고 확인 로직
            System.out.println("재고 확인 중...");
        };
    }

    @Bean
    public Action<OrderStates, OrderEvents> cancelOrderAction() {
        return context -> {
            // 주문 취소 로직
            System.out.println("결제 실패 - 주문 취소");
        };
    }

    @Bean
    public Action<OrderStates, OrderEvents> shipOrderAction() {
        return context -> {
            // 배송 처리 로직
            System.out.println("배송 요청 처리...");
        };
    }
}
