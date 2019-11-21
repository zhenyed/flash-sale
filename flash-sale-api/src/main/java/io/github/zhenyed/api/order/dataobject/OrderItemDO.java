package io.github.zhenyed.api.order.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.zhenyed.api.common.dataobject.DeletableDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@TableName("order_item")
@Data
@Accessors(chain = true)
public class OrderItemDO extends DeletableDO {

    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Integer singlePrice;
    private Integer totalPrice;

}
