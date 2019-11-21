package io.github.zhenyed.api.order.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.zhenyed.api.common.dataobject.DeletableDO;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("order1")
@Data
@Accessors(chain = true)
public class OrderDO extends DeletableDO {

    private Integer id;
    private Integer userId;
    private Integer total;
    /**
     * 订单状态
     *
     * 0 -- 未支付
     * 1 -- 已支付
     * 2 -- 订单超时
     */
    private Integer status;

    public enum Status {
        NOT_PAYMENT(0),
        ALREADY_PAYMENT(1),
        OVERDUE_PAYMENT(2);

        private final int code;
        Status(int code) {
            this.code = code;
        }

        public int getCode(){
            return code;
        }

        public boolean isValidStatus(Integer statusCode) {
            return statusCode < NOT_PAYMENT.getCode() || statusCode > OVERDUE_PAYMENT.getCode();
        }
    }
}
