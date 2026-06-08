package fyi.shamim.chatmemorywithspringaiv1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/6/26
 * Email: mdshamim723@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    private String orderId;
    private OrderStatus orderStatus;
    private String userId;
    private String userName;

}
