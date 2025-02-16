package ke.co.velnova.springstatemachine.config;

public enum OrderEvents {
    START_PAYMENT, PAYMENT_SUCCESS, PAYMENT_FAILED,
    CHECK_STOCK, STOCK_CONFIRMED, STOCK_FAILED,
    SHIP_ORDER, ORDER_COMPLETED, CANCEL_ORDER
}
