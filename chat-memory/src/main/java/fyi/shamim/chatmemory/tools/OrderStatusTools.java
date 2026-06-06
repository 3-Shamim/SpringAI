package fyi.shamim.chatmemory.tools;

import fyi.shamim.chatmemory.model.Order;
import fyi.shamim.chatmemory.model.OrderStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/6/26
 * Email: mdshamim723@gmail.com
 */

@Component
public class OrderStatusTools {

    private final Map<String, Order> orderMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

        for (int i = 1; i <= 10; i++) {

            String orderId = "ORD-" + i;

            orderMap.put(orderId, new Order(
                    orderId,
                    OrderStatus.CREATED,
                    "USR-" + i,
                    "User " + i
            ));

        }

    }

    @Tool(name = "get-order-status", description = "Get order status by Order ID and User ID")
    public String getOrderStatus(@ToolParam(description = "Order ID") String orderId,
                                 @ToolParam(description = "User ID") String userId) {

        Order order = orderMap.get(orderId);

        if (order == null) {
            return "Order not found by ID: " + orderId;
        }

        if (!order.getUserId().equals(userId)) {
            return "Order " + orderId + " not belongs to user " + userId;
        }

        OrderStatus currentStatus = order.getOrderStatus();

        order.setOrderStatus(nextStatus(currentStatus));
        orderMap.put(orderId, order);

        return String.format(
                "Order status of order ID %s is %s",
                orderId, currentStatus
        );
    }

    private OrderStatus nextStatus(OrderStatus orderStatus) {

        return switch (orderStatus) {
            case CREATED -> OrderStatus.PROCESSING;
            case PROCESSING -> OrderStatus.SHIPPING;
            case SHIPPING, DELIVERED -> OrderStatus.DELIVERED;
        };
    }


}
