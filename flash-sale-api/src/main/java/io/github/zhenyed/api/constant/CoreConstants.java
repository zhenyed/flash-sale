package io.github.zhenyed.api.constant;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

public class CoreConstants {
    public static final Integer productId = 1001;
    public static final Integer stock = 50;
    public static String createOrderMessage(Integer userId, Integer productId) {
        return JSON.toJSONString(new CoreModel(userId, productId));
    }

    public static CoreModel convertCreateOrderMessage(String msg) {
        return JSON.parseObject(msg, CoreModel.class);
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class CoreModel {
        Integer userId;
        Integer productId;
    }
}


